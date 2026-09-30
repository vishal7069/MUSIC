package com.dafli.app.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dafli.app.data.SearchResults
import com.dafli.app.ui.components.Note
import com.dafli.app.ui.components.SecondaryButton
import kotlinx.coroutines.CancellationException

class PagedMusic(
    val loaded: Loaded<SearchResults>, val loadingMore: Boolean,
    val moreError: String?, val loadMore: () -> Unit,
)

/** Keep already loaded songs visible while fetching or retrying the next provider page. */
@Composable
fun rememberPagedMusic(vararg keys: Any?, load: suspend (Int) -> SearchResults): PagedMusic {
    var page by remember(*keys) { mutableIntStateOf(1) }
    var attempt by remember(*keys) { mutableIntStateOf(0) }
    var state by remember(*keys) { mutableStateOf<Load<SearchResults>>(Load.Loading) }
    var loadingMore by remember(*keys) { mutableStateOf(false) }
    var moreError by remember(*keys) { mutableStateOf<String?>(null) }
    LaunchedEffect(*keys, page, attempt) {
        val previous = (state as? Load.Ok)?.value
        loadingMore = page > 1
        moreError = null
        try {
            val result = load(page)
            state = Load.Ok(if (page == 1 || previous == null) result else result.copy(
                tracks = (previous.tracks + result.tracks).distinctBy { it.id },
                artists = previous.artists, playlists = previous.playlists,
            ))
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            val message = "Couldn’t load music. Check your internet and try again."
            if (previous == null) state = Load.Failed(message) else moreError = message
        } finally {
            loadingMore = false
        }
    }
    return PagedMusic(Loaded(state) { attempt++ }, loadingMore, moreError) {
        if (!loadingMore) {
            if (moreError != null) attempt++
            else (state as? Load.Ok)?.value?.nextPage?.let { page = it }
        }
    }
}

@Composable
fun MoreSongs(music: PagedMusic) {
    val value = (music.loaded.state as? Load.Ok)?.value ?: return
    when {
        music.loadingMore -> Note("Loading more Hindi songs…")
        music.moreError != null -> ErrorCard(music.moreError, music.loadMore)
        value.nextPage != null -> SecondaryButton("Load more Hindi songs", Modifier.padding(16.dp), onClick = music.loadMore)
    }
}
