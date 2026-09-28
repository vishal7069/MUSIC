package com.dafli.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.dafli.app.theme.DColor
import com.dafli.app.theme.DType

/** Labelled dark text field (sign-up, log-in, playlist forms). */
@Composable
fun DField(
    label: String?,
    value: String,
    onValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    password: Boolean = false,
    helper: String? = null,
    helperColor: Color = DColor.TextDim,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    var reveal by remember { mutableStateOf(false) }
    Column(modifier.fillMaxWidth()) {
        if (label != null) { T(label, DType.Meta.copy(fontWeight = FontWeight.Medium), color = DColor.TextDim); Spacer(Modifier.height(8.dp)) }
        Row(
            Modifier.fillMaxWidth().heightIn(min = 52.dp).clip(RoundedCornerShape(12.dp)).background(DColor.Elevated)
                .border(1.dp, DColor.Line, RoundedCornerShape(12.dp)).padding(start = 16.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(Modifier.weight(1f).padding(vertical = 14.dp)) {
                if (value.isEmpty()) T(placeholder, DType.BodyL.copy(fontWeight = FontWeight.Normal), color = DColor.Disabled)
                BasicTextField(
                    value = value, onValueChange = onValue, singleLine = singleLine,
                    textStyle = DType.BodyL.copy(color = DColor.Text),
                    cursorBrush = SolidColor(DColor.Marigold),
                    visualTransformation = if (password && !reveal) PasswordVisualTransformation('•') else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(keyboardType = if (password) KeyboardType.Password else keyboardType),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            if (password) IconBtn(if (reveal) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility, if (reveal) "Hide password" else "Show password", tint = DColor.TextDim, size = 20.dp) { reveal = !reveal }
        }
        if (helper != null) T(helper, DType.Small, color = helperColor, modifier = Modifier.padding(top = 8.dp))
    }
}

/** Rounded search pill. [light] = the paper field used on the Search tab. */
@Composable
fun SearchField(
    value: String,
    onValue: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    light: Boolean = false,
    focus: FocusRequester? = null,
    readOnlyClick: (() -> Unit)? = null,
    onSearch: () -> Unit = {},
    trailing: (@Composable () -> Unit)? = null,
) {
    val fg = if (light) DColor.Night else DColor.Text
    val hint = if (light) Color(0xFF6E6978) else DColor.TextDim
    Row(
        modifier.fillMaxWidth().height(48.dp).clip(RoundedCornerShape(12.dp)).background(if (light) DColor.Paper else DColor.Elevated)
            .then(if (readOnlyClick != null) Modifier.clickable(onClick = readOnlyClick) else Modifier).padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, tint = if (light) DColor.Night else DColor.TextDim, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(10.dp))
        Box(Modifier.weight(1f)) {
            if (value.isEmpty()) T(placeholder, DType.BodyL.copy(fontWeight = FontWeight.Medium), color = hint, maxLines = 1)
            else if (readOnlyClick != null) T(value, DType.BodyL, color = fg, maxLines = 1)
            if (readOnlyClick == null) BasicTextField(
                value = value, onValueChange = onValue, singleLine = true,
                textStyle = DType.BodyL.copy(color = fg), cursorBrush = SolidColor(DColor.Marigold),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                modifier = Modifier.fillMaxWidth().then(if (focus != null) Modifier.focusRequester(focus) else Modifier),
            )
        }
        trailing?.invoke()
    }
}

