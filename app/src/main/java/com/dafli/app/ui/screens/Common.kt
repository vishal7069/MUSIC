package com.dafli.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import com.dafli.app.theme.DType
import com.dafli.app.ui.components.T
import com.dafli.app.ui.components.TextLink
import kotlinx.coroutines.CancellationException
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dafli.app.theme.DColor
import com.dafli.app.ui.components.LocalChromePad
import com.dafli.app.ui.components.TopBar

/** Pinned top bar + scrolling list that clears the fixed mini-player / nav. */
@Composable
fun ListScreen(
    title: String?,
    modifier: Modifier = Modifier,
    centerTitle: Boolean = false,
    state: LazyListState = rememberLazyListState(),
    actions: @Composable RowScope.() -> Unit = {},
    content: LazyListScope.() -> Unit,
) {
    Column(modifier.fillMaxSize().background(DColor.Night)) {
        TopBar(title, centerTitle = centerTitle, actions = actions)
        LazyColumn(
            Modifier.weight(1f), state = state,
            contentPadding = PaddingValues(bottom = LocalChromePad.current + 24.dp),
            content = content,
        )
    }
}

/** Handy spacer item. */
fun LazyListScope.gap(h: Int) = item { Spacer(Modifier.height(h.dp)) }

// ---------------------------------------------------------------- loading remote data

sealed interface Load<out T> {
    data object Loading : Load<Nothing>
    data class Ok<T>(val value: T) : Load<T>
    data class Failed(val message: String) : Load<Nothing>
}

class Loaded<T>(val state: Load<T>, val retry: () -> Unit)

/** Runs [block] when [keys] change and exposes Loading / Ok / Failed plus a retry. */
@Composable
fun <T> rememberLoad(vararg keys: Any?, block: suspend () -> T): Loaded<T> {
    var attempt by remember { mutableIntStateOf(0) }
    var state by remember(*keys) { mutableStateOf<Load<T>>(Load.Loading) }
    LaunchedEffect(*keys, attempt) {
        state = Load.Loading
        state = try { Load.Ok(block()) } catch (e: CancellationException) { throw e } catch (e: Exception) {
            Load.Failed("Couldn’t load music. Check your internet and try again.")
        }
    }
    return Loaded(state) { attempt++ }
}

/** Shows a shimmer-free skeleton while loading, a retry card on failure, else [content]. */
@Composable
fun <T> LoadBox(l: Loaded<T>, loadingHeight: Int = 160, content: @Composable (T) -> Unit) {
    when (val s = l.state) {
        is Load.Ok -> content(s.value)
        Load.Loading -> Box(
            Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth().height(loadingHeight.dp)
                .clip(RoundedCornerShape(12.dp)).background(DColor.Surface),
        )
        is Load.Failed -> ErrorCard(s.message, l.retry)
    }
}

@Composable
fun ErrorCard(message: String, retry: () -> Unit) {
    Row(
        Modifier.padding(16.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(DColor.Surface).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        T(message, DType.Meta, Modifier.weight(1f), color = DColor.TextDim)
        TextLink("Retry", Modifier.padding(start = 8.dp), onClick = retry)
    }
}
