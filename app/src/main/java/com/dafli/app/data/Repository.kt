package com.dafli.app.data

import com.dafli.app.data.remote.AudiusApi

/** Hindi discovery categories shown in onboarding and Browse. `api` is a search query. */
data class Genre(val name: String, val api: String, val art: Art)

val Genres = listOf(
    Genre("All Hindi songs", "Hindi", Art.Bollywood),
    Genre("Bollywood", "Hindi hits", Art.Bollywood),
    Genre("Romantic", "Hindi romantic", Art.B),
    Genre("Sad songs", "Hindi sad", Art.H),
    Genre("Party", "Hindi party", Art.Workout),
    Genre("Retro", "Hindi retro", Art.Retro),
    Genre("Devotional", "Hindi bhajan", Art.Devotional),
    Genre("Lo-fi", "Hindi lofi", Art.LoFi),
    Genre("Hip-Hop", "Hindi rap", Art.HipHop),
    Genre("Indie", "Hindi indie", Art.Indie),
    Genre("Chill", "Hindi chill", Art.C),
    Genre("Acoustic", "Hindi acoustic", Art.E),
    Genre("Ghazals", "Hindi ghazal", Art.A),
)

/** Where the screens get music from. Swap implementations for tests and previews. */
interface MusicRepository {
    suspend fun trending(genre: String? = null, limit: Int = 20): List<Track>
    suspend fun trendingPlaylists(): List<Collection>
    suspend fun playlistTracks(c: Collection): List<Track>
    suspend fun artist(id: String): Artist
    suspend fun artistTracks(id: String): List<Track>
    suspend fun search(q: String): SearchResults
    suspend fun searchPage(q: String, page: Int): SearchResults =
        if (page == 1) search(q) else SearchResults(emptyList(), emptyList(), emptyList())
    suspend fun browse(q: String, page: Int): SearchResults = searchPage(q, page)
    suspend fun artistSongsPage(id: String, page: Int): SearchResults =
        SearchResults(if (page == 1) artistTracks(id) else emptyList(), emptyList(), emptyList())
    suspend fun playlistSongsPage(c: Collection, page: Int): SearchResults =
        SearchResults(if (page == 1) playlistTracks(c) else emptyList(), emptyList(), emptyList())
}

data class SearchResults(
    val tracks: List<Track>, val artists: List<Artist>, val playlists: List<Collection>,
    /** Provider matches can include covers, remixes and languages filtered out of this app. */
    val totalMatches: Int = 0,
    val nextPage: Int? = null,
)

class AudiusRepository(private val api: AudiusApi = AudiusApi()) : MusicRepository {
    // Small in-memory cache so going back to a screen is instant.
    private val cache = HashMap<String, Any>()

    @Suppress("UNCHECKED_CAST")
    private suspend fun <T : Any> cached(key: String, load: suspend () -> T): T =
        (cache[key] as T?) ?: load().also { cache[key] = it }

    override suspend fun trending(genre: String?, limit: Int) = cached("t:$genre:$limit") { api.trendingTracks(genre, limit = limit) }
    override suspend fun trendingPlaylists() = cached("tp") { api.trendingPlaylists() }
    override suspend fun playlistTracks(c: Collection): List<Track> = cached("pl:${c.id}") {
        when {
            c.remoteId != null -> api.playlistTracks(c.remoteId)
            c.genre != null -> api.trendingTracks(c.genre)
            else -> api.trendingTracks()
        }
    }
    override suspend fun artist(id: String) = cached("a:$id") { api.artist(id) }
    override suspend fun artistTracks(id: String) = cached("at:$id") { api.artistTracks(id) }
    override suspend fun search(q: String): SearchResults = cached("s:${q.lowercase()}") {
        SearchResults(api.searchTracks(q), api.searchArtists(q), api.searchPlaylists(q))
    }
}

/** Offline sample catalogue (previews, screenshots, tests). */
class SampleRepository : MusicRepository {
    override suspend fun trending(genre: String?, limit: Int) = Sample.allTracks.take(limit)
    override suspend fun trendingPlaylists() = Sample.madeForYouAll
    override suspend fun playlistTracks(c: Collection) = Sample.allTracks.shuffled(kotlin.random.Random(c.id.hashCode()))
    override suspend fun artist(id: String) = Sample.kabirSen
    override suspend fun artistTracks(id: String) = Sample.allTracks.filter { it.artist.startsWith("Kabir") }
    override suspend fun search(q: String) = SearchResults(
        Sample.allTracks.filter { it.title.contains(q, true) || it.artist.contains(q, true) },
        Sample.artists.filter { it.name.contains(q, true) },
        Sample.madeForYouAll.filter { it.title.contains(q, true) },
    )
}
