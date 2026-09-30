package com.dafli.app.data.remote

import com.dafli.app.data.SaavnRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

class SaavnApiTest {
    private fun song(id: String, language: String = "hindi", playable: Boolean = true) = JSONObject().apply {
        put("id", id); put("name", "गीत &amp; &quot;Dil&quot;"); put("language", language); put("duration", 268)
        put("album", JSONObject().put("name", "Hindi album"))
        put("artists", JSONObject().put("primary", JSONArray().put(JSONObject().put("id", "123").put("name", "Singer"))))
        put("downloadUrl", if (playable) JSONArray()
            .put(JSONObject().put("quality", "48kbps").put("url", "https://example.com/48.mp4"))
            .put(JSONObject().put("quality", "160kbps").put("url", "https://example.com/160.mp4"))
            .put(JSONObject().put("quality", "320kbps").put("url", "https://example.com/320.mp4")) else JSONArray())
    }
    private fun response(data: JSONObject) = JSONObject().put("success", true).put("data", data)

    @Test fun filtersEnglishUnplayableAndDuplicates() {
        val tracks = JSONArray().put(song("a")).put(song("b", "english")).put(song("c", playable = false))
            .put(song("a")).put(song("d", "HINDI")).hindiTracks()
        assertEquals(listOf("sa:a", "sa:d"), tracks.map { it.id })
        assertEquals("गीत & \"Dil\"", tracks.first().title)
        assertEquals("https://example.com/160.mp4", tracks.first().streamUrl)
        assertEquals("sa:123", tracks.first().artistId)
    }

    @Test fun missingPreferredQualityUsesAvailableSecureUrl() {
        val track = song("a").put("downloadUrl", JSONArray()
            .put(JSONObject().put("quality", "96kbps").put("url", "https://example.com/96.mp4")))
        assertEquals("https://example.com/96.mp4", track.toTrack()?.streamUrl)
        assertNull(song("a").put("downloadUrl", JSONArray()
            .put(JSONObject().put("quality", "160kbps").put("url", "http://example.com/audio"))).toTrack())
    }

    @Test fun homeSearchesHindiCatalogueWithoutChartLimit() = runBlocking {
        val api = SaavnApi(request = { path, params ->
            assertEquals("/search/songs", path)
            assertEquals("Hindi", params["query"])
            response(JSONObject().put("results", JSONArray().put(song("a")).put(song("b", "english"))))
        })
        val tracks = SaavnRepository(api).trending(limit = 5)
        assertEquals(listOf("sa:a"), tracks.map { it.id })
    }

    @Test fun pagesBeyondFiftyAreAvailableWithoutSkippingOffsets() = runBlocking {
        val api = SaavnApi(request = { _, params ->
            assertEquals("40", params["limit"])
            val page = params.getValue("page").toInt()
            response(JSONObject().put("total", 125).put("results", JSONArray().apply {
                repeat(if (page == 4) 5 else 40) { put(song("${page * 40 + it}")) }
            }))
        })
        val repo = SaavnRepository(api)
        val first = repo.browse("Hindi", 1)
        val second = repo.browse("Hindi", first.nextPage!!)
        val last = repo.browse("Hindi", 4)
        assertEquals(80, (first.tracks + second.tracks).distinctBy { it.id }.size)
        assertEquals(3, second.nextPage)
        assertNull(last.nextPage)
    }

    @Test fun emptyHindiPageDoesNotHideLaterProviderPages() = runBlocking {
        val api = SaavnApi(request = { _, _ -> response(JSONObject().put("total", 100)
            .put("results", JSONArray().put(song("a", "english")))) })
        val page = api.searchTracksPage("Singer", 1)
        assertTrue(page.tracks.isEmpty())
        assertEquals(2, page.nextPage)
    }

    @Test fun categoryAndLegacyPreferenceSearchHindi() = runBlocking {
        val queries = mutableListOf<String>()
        val api = SaavnApi(request = { _, params ->
            queries.add(params.getValue("query"))
            response(JSONObject().put("results", JSONArray().put(song("a"))))
        })
        val repo = SaavnRepository(api)
        repo.trending("Hindi romantic", 10)
        repo.trending("Hip-Hop/Rap", 10)
        assertEquals(listOf("Hindi romantic", "Hindi rap"), queries)
    }

    @Test fun optionalSearchFailureDoesNotHideSongs() = runBlocking {
        val api = SaavnApi(request = { path, _ ->
            if (path != "/search/songs") throw ApiException("Unavailable")
            response(JSONObject().put("results", JSONArray().put(song("a"))))
        })
        val result = SaavnRepository(api).search(" Kesariya ")
        assertEquals(1, result.tracks.size)
        assertTrue(result.artists.isEmpty()); assertTrue(result.playlists.isEmpty())
    }

    @Test fun apiFailureRemainsRetryable() = runBlocking {
        var calls = 0
        val api = SaavnApi(request = { _, _ ->
            calls++
            if (calls == 1) JSONObject().put("success", false)
            else response(JSONObject().put("results", JSONArray().put(song("a"))))
        })
        val repo = SaavnRepository(api)
        try { repo.trending("Hindi romantic", 5); fail("Expected service error") } catch (_: ApiException) { }
        assertEquals(1, repo.trending("Hindi romantic", 5).size)
        assertEquals(2, calls)
    }

    @Test fun cancelledSearchIsNotSwallowed() = runBlocking {
        val api = SaavnApi(request = { _, _ -> throw CancellationException("Cancelled") })
        try { SaavnRepository(api).search("Kesariya"); fail("Expected cancellation") }
        catch (_: CancellationException) { }
    }

    @Test fun decodesHindiNumericEntities() {
        assertEquals("हिंदी & 'song'", "&#x939;&#2367;&#2306;&#2342;&#2368; &amp; &#039;song&#039;".decoded())
    }
}
