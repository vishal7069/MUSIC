package com.dafli.app.data

import com.dafli.app.data.remote.SaavnApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.ConcurrentHashMap

/** Hindi discovery and playback. Never falls back to global English trending. */
class SaavnRepository(private val api: SaavnApi = SaavnApi()) : MusicRepository {
    private data class Cached(val value: Any, val expires: Long)
    private val cache = ConcurrentHashMap<String, Cached>()

    @Suppress("UNCHECKED_CAST")
    private suspend fun <T : Any> cached(key: String, load: suspend () -> T): T {
        val now = System.nanoTime()
        cache[key]?.takeIf { it.expires > now }?.let { return it.value as T }
        val value = load()
        if (cache.size >= 100) cache.clear()
        cache[key] = Cached(value, System.nanoTime() + 300_000_000_000L)
        return value
    }

    override suspend fun trending(genre: String?, limit: Int): List<Track> {
        if (limit <= 0) return emptyList()
        return cached("tracks:$genre:$limit") {
            api.searchTracks(if (genre == null || genre == "Hindi") "Hindi" else hindiCategoryQuery(genre), limit)
        }
    }

    override suspend fun trendingPlaylists() = cached("playlists") { api.searchPlaylists("Hindi") }

    override suspend fun playlistTracks(c: Collection): List<Track> = cached("playlist:${c.id}") {
        when {
            c.remoteId?.startsWith("sa:") == true -> api.playlistTracks(c.remoteId)
            c.genre != null -> trending(c.genre, 50)
            else -> trending(limit = 50)
        }
    }

    // Old Audius likes/history keep their audio URLs and artist links after the update.
    private val legacy by lazy { AudiusRepository() }
    override suspend fun artist(id: String): Artist = cached("artist:$id") {
        if (id.startsWith("sa:")) api.artist(id) else legacy.artist(id)
    }
    override suspend fun artistTracks(id: String): List<Track> = cached("artistTracks:$id") {
        if (id.startsWith("sa:")) api.artistTracks(id) else legacy.artistTracks(id)
    }

    override suspend fun search(q: String): SearchResults = searchPage(q, 1)

    override suspend fun searchPage(q: String, page: Int): SearchResults {
        val query = q.trim()
        if (query.isBlank()) return SearchResults(emptyList(), emptyList(), emptyList())
        return cached("search:${query.lowercase()}:$page") {
            coroutineScope {
                val tracks = async { api.searchTracksPage(query, page) }
                val artists = async { if (page == 1) optional { api.searchArtists(query) } else emptyList() }
                val playlists = async { if (page == 1) optional { api.searchPlaylists(query) } else emptyList() }
                tracks.await().copy(artists = artists.await(), playlists = playlists.await())
            }
        }
    }

    override suspend fun browse(q: String, page: Int): SearchResults = cached("browse:$q:$page") {
        api.searchTracksPage(hindiCategoryQuery(q), page)
    }

    override suspend fun artistSongsPage(id: String, page: Int): SearchResults = cached("artistPage:$id:$page") {
        if (id.startsWith("sa:")) api.artistTracksPage(id, page)
        else SearchResults(if (page == 1) legacy.artistTracks(id) else emptyList(), emptyList(), emptyList())
    }

    override suspend fun playlistSongsPage(c: Collection, page: Int): SearchResults = cached("playlistPage:${c.id}:$page") {
        if (c.remoteId?.startsWith("sa:") == true) api.playlistTracksPage(c.remoteId, page)
        else browse(c.genre ?: "Hindi", page)
    }

    private suspend fun <T> optional(load: suspend () -> List<T>): List<T> = try { load() }
    catch (e: CancellationException) { throw e }
    catch (_: Exception) { emptyList() }

}

/** Keep taste preferences from earlier Audius builds useful after the provider change. */
fun hindiCategoryQuery(genre: String): String = when (genre) {
        "Lo-Fi" -> "Hindi lofi"
        "Hip-Hop/Rap" -> "Hindi rap"
        "Electronic", "Dance & EDM" -> "Hindi party"
        "Pop", "World" -> "Hindi hits"
        "Hindi" -> "Hindi"
        "Devotional" -> "Hindi bhajan"
        "R&B/Soul" -> "Hindi romantic"
        "Alternative" -> "Hindi indie"
        "Ambient" -> "Hindi chill"
        "Classical" -> "Hindi ghazal"
        "Acoustic" -> "Hindi acoustic"
        else -> if (genre.startsWith("Hindi", true)) genre else "Hindi $genre"
}
