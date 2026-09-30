package com.dafli.app.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dafli.app.platform.KeyValueStore
import org.json.JSONArray
import org.json.JSONObject

/**
 * The user's own stuff, saved on the phone: liked songs, recently played, picked genres, name.
 * No account or server needed – nothing leaves the device.
 */
class LocalLibrary(private val store: KeyValueStore) {
    val liked = mutableStateListOf<Track>().apply { addAll(load("liked")) }
    val recent = mutableStateListOf<Track>().apply { addAll(load("recent")) }
    val genres = mutableStateListOf<String>().apply {
        addAll(store.getString("genres")?.split("|")?.filter { it.isNotBlank() }
            ?.map { hindiCategoryQuery(it) }?.distinct().orEmpty())
    }
    val searches = mutableStateListOf<String>().apply { addAll(store.getString("searches")?.split("\n")?.filter { it.isNotBlank() }.orEmpty()) }

    fun addSearch(q: String) {
        val t = q.trim(); if (t.isEmpty()) return
        searches.removeAll { it.equals(t, true) }; searches.add(0, t)
        while (searches.size > 8) searches.removeAt(searches.lastIndex)
        store.putString("searches", searches.joinToString("\n"))
    }

    fun removeSearch(q: String) { searches.remove(q); store.putString("searches", searches.joinToString("\n")) }
    fun clearSearches() { searches.clear(); store.putString("searches", null) }

    var onboarded by mutableStateOf(store.getString("onboarded") == "1")
        private set
    var name by mutableStateOf(store.getString("name") ?: "")
        private set
    var autoplay by mutableStateOf(store.getString("autoplay") != "0")
        private set

    fun isLiked(t: Track) = liked.any { it.id == t.id }

    fun toggleLike(t: Track) {
        val i = liked.indexOfFirst { it.id == t.id }
        if (i >= 0) liked.removeAt(i) else liked.add(0, t)
        save("liked", liked)
    }

    fun addRecent(t: Track) {
        if (t.streamUrl == null) return
        recent.removeAll { it.id == t.id }
        recent.add(0, t)
        while (recent.size > 50) recent.removeAt(recent.lastIndex)
        save("recent", recent)
    }

    fun clearRecent() { recent.clear(); save("recent", recent) }

    fun setGenres(list: List<String>) {
        genres.clear(); genres.addAll(list)
        store.putString("genres", list.joinToString("|"))
    }

    fun finishOnboarding(displayName: String) {
        name = displayName.trim(); store.putString("name", name)
        onboarded = true; store.putString("onboarded", "1")
    }

    fun rename(n: String) { name = n.trim(); store.putString("name", name) }
    fun setAutoplayPref(on: Boolean) { autoplay = on; store.putString("autoplay", if (on) "1" else "0") }

    fun resetAll() {
        liked.clear(); recent.clear(); genres.clear(); searches.clear()
        listOf("liked", "recent", "genres", "onboarded", "name", "searches").forEach { store.putString(it, null) }
        onboarded = false; name = ""
    }

    // ---------------------------------------------------------------- json

    private fun save(key: String, list: List<Track>) {
        val arr = JSONArray()
        list.forEach { t ->
            arr.put(JSONObject().apply {
                put("id", t.id); put("title", t.title); put("artist", t.artist); put("dur", t.durationSec)
                put("art", t.artUrl ?: ""); put("stream", t.streamUrl ?: ""); put("artistId", t.artistId ?: "")
                put("genre", t.genre ?: ""); put("share", t.shareUrl ?: ""); put("fallback", t.art.name)
            })
        }
        store.putString(key, arr.toString())
    }

    private fun load(key: String): List<Track> = runCatching {
        val arr = JSONArray(store.getString(key) ?: return emptyList())
        (0 until arr.length()).map { i ->
            val o = arr.getJSONObject(i)
            Track(
                id = o.getString("id"), title = o.optString("title"), artist = o.optString("artist"),
                art = runCatching { Art.valueOf(o.optString("fallback")) }.getOrDefault(Art.A),
                durationSec = o.optInt("dur"),
                artUrl = o.optString("art").ifBlank { null }, streamUrl = o.optString("stream").ifBlank { null },
                artistId = o.optString("artistId").ifBlank { null }, genre = o.optString("genre").ifBlank { null },
                shareUrl = o.optString("share").ifBlank { null },
            )
        }
    }.getOrDefault(emptyList())
}
