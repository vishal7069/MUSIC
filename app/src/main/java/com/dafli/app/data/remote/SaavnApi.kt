package com.dafli.app.data.remote

import com.dafli.app.AppConfig
import com.dafli.app.data.Art
import com.dafli.app.data.Artist
import com.dafli.app.data.Collection
import com.dafli.app.data.Track
import com.dafli.app.data.SearchResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/** Community JioSaavn API, not an official JioSaavn service. No API key required. */
class SaavnApi(
    private val base: String = AppConfig.SAAVN_API_URL,
    private val request: ((String, Map<String, String>) -> JSONObject)? = null,
) {
    private suspend fun get(path: String, params: Map<String, String> = emptyMap()): JSONObject = withContext(Dispatchers.IO) {
        val response = if (request != null) request.invoke(path, params) else {
            val query = params.entries.joinToString("&") { "${it.key}=${URLEncoder.encode(it.value, "UTF-8")}" }
            val conn = URL("${base.trimEnd('/')}$path?$query").openConnection() as HttpURLConnection
            conn.connectTimeout = 10_000
            conn.readTimeout = 15_000
            conn.setRequestProperty("Accept", "application/json")
            try {
                if (conn.responseCode !in 200..299) throw ApiException("Music service unavailable (${conn.responseCode})")
                JSONObject(conn.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() })
            } finally {
                conn.disconnect()
            }
        }
        if (!response.optBoolean("success", false)) throw ApiException("Music service returned an error")
        response.optJSONObject("data") ?: throw ApiException("Music service returned invalid data")
    }

    suspend fun searchTracks(query: String, limit: Int = 20): List<Track> =
        searchTracksPage(query, 1).tracks.take(limit.coerceIn(0, PAGE_SIZE))

    suspend fun searchTracksPage(query: String, page: Int): SearchResults {
        require(page >= 1)
        return get("/search/songs", mapOf("query" to query, "page" to "$page", "limit" to "$PAGE_SIZE"))
            .songPage("results", page)
    }

    suspend fun searchArtists(query: String): List<Artist> =
        get("/search/artists", mapOf("query" to query, "page" to "1", "limit" to "10"))
            .optJSONArray("results").objects().map { it.toArtist() }

    suspend fun searchPlaylists(query: String): List<Collection> =
        get("/search/playlists", mapOf("query" to query, "page" to "1", "limit" to "20"))
            .optJSONArray("results").objects().filter { it.optString("language").equals("hindi", true) }
            .map { it.toCollection() }

    suspend fun playlistTracks(id: String): List<Track> = playlistTracksPage(id, 1).tracks

    suspend fun playlistTracksPage(id: String, page: Int): SearchResults {
        require(page >= 1)
        val data = get("/playlists", mapOf("id" to id.removePrefix("sa:"), "limit" to "$PAGE_SIZE", "page" to "$page"))
        val songs = data.optJSONArray("songs")
        // This API's songCount is the current page size, not the whole playlist size.
        return SearchResults(songs.hindiTracks(), emptyList(), emptyList(),
            nextPage = if ((songs?.length() ?: 0) >= PAGE_SIZE) page + 1 else null)
    }

    suspend fun artist(id: String): Artist = get("/artists/${encodeId(id)}").toArtist()

    suspend fun artistTracks(id: String): List<Track> = artistTracksPage(id, 1).tracks

    suspend fun artistTracksPage(id: String, page: Int): SearchResults {
        require(page >= 1)
        return get("/artists/${encodeId(id)}/songs", mapOf("sortBy" to "popularity", "page" to "$page", "limit" to "$PAGE_SIZE"))
            .songPage("songs", page)
    }

    private fun encodeId(id: String) = URLEncoder.encode(id.removePrefix("sa:"), "UTF-8")

    private fun JSONObject.songPage(key: String, page: Int): SearchResults {
        val raw = optJSONArray(key)
        val total = optInt("total")
        return SearchResults(raw.hindiTracks(), emptyList(), emptyList(), total,
            if ((raw?.length() ?: 0) > 0 && page.toLong() * PAGE_SIZE < total) page + 1 else null)
    }

    companion object {
        // The upstream service caps search at 40. Asking for 50 skips songs between pages.
        const val PAGE_SIZE = 40
    }
}

internal fun JSONArray?.objects(): List<JSONObject> =
    if (this == null) emptyList() else (0 until length()).mapNotNull { optJSONObject(it) }

internal fun JSONArray?.hindiTracks(): List<Track> = objects()
    .filter { it.optString("language").equals("hindi", true) }
    .mapNotNull { it.toTrack() }.distinctBy { it.id }

private fun JSONObject.media(key: String, preferred: String): String? {
    val options = optJSONArray(key).objects()
    val chosen = options.firstOrNull { it.optString("quality") == preferred && it.optString("url").startsWith("https://") }
        ?: options.lastOrNull { it.optString("url").startsWith("https://") }
    return chosen?.optString("url")
}

internal fun JSONObject.toTrack(): Track? {
    val id = optString("id").takeIf { it.isNotBlank() } ?: return null
    val title = optString("name").decoded().takeIf { it.isNotBlank() } ?: return null
    val stream = media("downloadUrl", "160kbps") ?: return null
    val artists = optJSONObject("artists")
    val primary = artists?.optJSONArray("primary").objects()
    val singer = artists?.optJSONArray("all").objects().firstOrNull { it.optString("role") == "singer" }
        ?: primary.firstOrNull()
    return Track(
        id = "sa:$id", title = title,
        artist = primary.joinToString(", ") { it.optString("name").decoded() }.ifBlank { singer?.optString("name").orEmpty().decoded() },
        art = Art.Bollywood, durationSec = optInt("duration"),
        explicit = optBoolean("explicitContent"),
        album = optJSONObject("album")?.optString("name")?.decoded() ?: title,
        artUrl = media("image", "500x500"), streamUrl = stream,
        artistId = singer?.optString("id")?.takeIf { it.isNotBlank() }?.let { "sa:$it" },
        genre = "Hindi", shareUrl = optString("url").takeIf { it.startsWith("https://") },
    )
}

internal fun JSONObject.toArtist() = Artist(
    id = "sa:${getString("id")}", name = optString("name").decoded(), art = Art.Bollywood,
    artUrl = media("image", "500x500"), followers = optInt("followerCount"),
    bio = optJSONArray("bio").objects().joinToString("\n\n") { it.optString("text").decoded() }.ifBlank { null },
)

internal fun JSONObject.toCollection() = Collection(
    id = "sap:${getString("id")}", title = optString("name").decoded(),
    subtitle = "Hindi · ${optInt("songCount")} songs", art = Art.Bollywood,
    artUrl = media("image", "500x500"), remoteId = "sa:${getString("id")}",
    shareUrl = optString("url").takeIf { it.startsWith("https://") },
)

/** Display provider titles as text, including Devanagari and HTML-encoded punctuation. */
internal fun String.decoded(): String = replace("&quot;", "\"").replace("&#039;", "'")
    .replace("&apos;", "'").replace("&lt;", "<").replace("&gt;", ">").replace("&nbsp;", " ")
    .replace(Regex("&#(x[0-9a-fA-F]+|[0-9]+);")) { match ->
        val value = match.groupValues[1]
        val code = if (value.startsWith("x")) value.drop(1).toIntOrNull(16) else value.toIntOrNull()
        if (code != null && Character.isValidCodePoint(code)) String(Character.toChars(code)) else match.value
    }.replace("&amp;", "&")
