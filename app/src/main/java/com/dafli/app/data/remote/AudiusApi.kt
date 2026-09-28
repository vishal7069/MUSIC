package com.dafli.app.data.remote

import com.dafli.app.data.Art
import com.dafli.app.data.Artist
import com.dafli.app.data.Collection
import com.dafli.app.data.Kind
import com.dafli.app.data.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/**
 * Minimal client for the public Audius REST API (https://docs.audius.co).
 * Free, no key needed – every request carries `app_name`.
 * Uses only HttpURLConnection + org.json so it has no extra dependencies.
 */
class AudiusApi(
    private val appName: String = "Dafli",
    private val base: String = "https://api.audius.co/v1",
) {
    private suspend fun get(path: String, params: Map<String, String?> = emptyMap()): JSONObject = withContext(Dispatchers.IO) {
        val query = (params + ("app_name" to appName)).filterValues { !it.isNullOrBlank() }
            .entries.joinToString("&") { "${it.key}=${URLEncoder.encode(it.value, "UTF-8")}" }
        val conn = URL("$base$path?$query").openConnection() as HttpURLConnection
        conn.connectTimeout = 10_000
        conn.readTimeout = 15_000
        conn.setRequestProperty("Accept", "application/json")
        try {
            val code = conn.responseCode
            if (code !in 200..299) throw ApiException("Audius $code on $path")
            JSONObject(conn.inputStream.bufferedReader().use { it.readText() })
        } finally {
            conn.disconnect()
        }
    }

    private fun JSONObject.list(): JSONArray = optJSONArray("data") ?: JSONArray()

    suspend fun trendingTracks(genre: String? = null, time: String = "week", limit: Int = 20): List<Track> =
        get("/tracks/trending", mapOf("genre" to genre, "time" to time, "limit" to "$limit")).list().tracks()

    suspend fun searchTracks(q: String, limit: Int = 20): List<Track> =
        get("/tracks/search", mapOf("query" to q, "limit" to "$limit")).list().tracks()

    suspend fun searchArtists(q: String, limit: Int = 10): List<Artist> =
        get("/users/search", mapOf("query" to q, "limit" to "$limit")).list().map { it.artist() }

    suspend fun searchPlaylists(q: String, limit: Int = 10): List<Collection> =
        get("/playlists/search", mapOf("query" to q, "limit" to "$limit")).list().map { it.playlist() }

    suspend fun trendingPlaylists(limit: Int = 12): List<Collection> =
        get("/playlists/trending", mapOf("limit" to "$limit", "time" to "week")).list().map { it.playlist() }

    suspend fun playlistTracks(playlistId: String): List<Track> =
        get("/playlists/$playlistId/tracks").list().tracks()

    suspend fun artist(userId: String): Artist =
        get("/users/$userId").getJSONObject("data").artist()

    suspend fun artistTracks(userId: String, limit: Int = 20): List<Track> =
        get("/users/$userId/tracks", mapOf("limit" to "$limit", "sort" to "plays")).list().tracks()

    // ---------------------------------------------------------------- mapping

    private fun JSONArray.tracks(): List<Track> = map { it.track() }.filter { it.streamUrl != null }

    private fun JSONArray.map(): List<JSONObject> = (0 until length()).mapNotNull { optJSONObject(it) }
    private fun <T> JSONArray.map(f: (JSONObject) -> T): List<T> = map().map(f)

    private fun JSONObject.img(key: String): String? =
        optJSONObject(key)?.let { it.optString("480x480").ifBlank { it.optString("150x150") }.ifBlank { null } }

    private fun JSONObject.track(): Track {
        val id = getString("id")
        val user = optJSONObject("user")
        val streamable = optBoolean("is_streamable", true) && !optBoolean("is_stream_gated", false)
        return Track(
            id = "au:$id",
            title = optString("title"),
            artist = user?.optString("name").orEmpty(),
            art = fallbackArt(id),
            durationSec = optInt("duration"),
            album = optString("title"),
            artUrl = img("artwork") ?: user?.img("profile_picture"),
            streamUrl = if (streamable) "$base/tracks/$id/stream?app_name=$appName" else null,
            artistId = user?.optString("id"),
            genre = optString("genre").ifBlank { null },
            shareUrl = optString("permalink").ifBlank { null }?.let { "https://audius.co$it" },
        )
    }

    private fun JSONObject.artist(): Artist = Artist(
        name = optString("name"),
        art = fallbackArt(optString("id")),
        tagline = listOfNotNull("Artist", optString("location").ifBlank { null }).joinToString(" · "),
        id = optString("id"),
        artUrl = img("profile_picture"),
        followers = optInt("follower_count"),
        bio = optString("bio").ifBlank { null },
    )

    private fun JSONObject.playlist(): Collection {
        val id = getString("id")
        val by = optJSONObject("user")?.optString("name").orEmpty()
        val n = optInt("track_count")
        return Collection(
            id = "aup:$id",
            title = optString("playlist_name"),
            subtitle = listOf(by, if (n > 0) "$n songs" else "").filter { it.isNotBlank() }.joinToString(" · "),
            art = fallbackArt(id),
            kind = if (optBoolean("is_album")) Kind.Album else Kind.Playlist,
            artUrl = img("artwork"),
            remoteId = id,
        )
    }

    companion object {
        private val fallbacks = listOf(Art.A, Art.B, Art.C, Art.D, Art.F, Art.G, Art.H, Art.I, Art.J, Art.K)
        fun fallbackArt(id: String): Art = fallbacks[(id.hashCode() and 0x7fffffff) % fallbacks.size]
    }
}

class ApiException(message: String) : Exception(message)
