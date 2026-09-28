package com.dafli.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dafli.app.data.Art
import com.dafli.app.data.Sample
import com.dafli.app.nav.Route
import com.dafli.app.theme.DColor
import com.dafli.app.theme.DType
import com.dafli.app.ui.brand.DafliLockup
import com.dafli.app.ui.components.ArtImage
import com.dafli.app.ui.components.Chip
import com.dafli.app.ui.components.DField
import com.dafli.app.ui.components.LocalNav
import com.dafli.app.ui.components.PrimaryButton
import com.dafli.app.ui.components.SearchField
import com.dafli.app.ui.components.SecondaryButton
import com.dafli.app.ui.components.T
import com.dafli.app.ui.components.TextLink
import com.dafli.app.ui.components.TopBar

// ------------------------------------------------------------------ S02 Welcome

@Composable
fun WelcomeScreen() {
    val nav = LocalNav.current
    Box(Modifier.fillMaxSize().background(DColor.Night)) {
        ArtImage(Art.Hero, Modifier.fillMaxWidth().height(560.dp), RoundedCornerShape(0.dp))
        Box(
            Modifier.fillMaxWidth().height(560.dp).background(
                Brush.verticalGradient(0f to DColor.Night.copy(alpha = 0.35f), 0.45f to DColor.Night.copy(alpha = 0.15f), 1f to DColor.Night)
            )
        )
        Column(
            Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Bottom,
        ) {
            DafliLockup(42.dp)
            Spacer(Modifier.height(16.dp))
            T("Every song.\nFree. No limits.", DType.H1.copy(fontSize = 36.sp, lineHeight = 42.sp))
            Spacer(Modifier.height(12.dp))
            T("Hindi, Punjabi, Tamil and more. Play any song, any time, without paying.", DType.Body, color = DColor.TextDim)
            Spacer(Modifier.height(28.dp))
            PrimaryButton("Sign up free") { nav.go(Route.SignUp) }
            Spacer(Modifier.height(12.dp))
            SecondaryButton("Continue with Google", icon = Icons.Rounded.Language) { nav.resetTo(Route.Preferences) }
            Spacer(Modifier.height(12.dp))
            SecondaryButton("Continue with phone number", icon = Icons.Rounded.PhoneAndroid) { nav.go(Route.SignUp) }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) { TextLink("Log in") { nav.go(Route.LogIn) } }
                Box(Modifier.width(1.dp).height(20.dp).background(DColor.Line))
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) { TextLink("Browse first", color = DColor.TextDim) { nav.resetTo(Route.Home) } }
            }
            Spacer(Modifier.height(12.dp))
            T(
                "By continuing you agree to the Terms of Use and Privacy Policy.", DType.Tiny.copy(fontWeight = FontWeight.Normal),
                Modifier.fillMaxWidth().padding(horizontal = 16.dp), color = DColor.Disabled, align = TextAlign.Center,
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

// ------------------------------------------------------------------ S03 Create account

@Composable
fun SignUpScreen() {
    val nav = LocalNav.current
    var email by rememberSaveable { mutableStateOf(Sample.email) }
    var password by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf(Sample.me.name) }
    var agree by rememberSaveable { mutableStateOf(false) }
    var news by rememberSaveable { mutableStateOf(false) }
    val longEnough = password.length >= 12
    val valid = agree && longEnough && "@" in email && name.isNotBlank()

    Column(Modifier.fillMaxSize().background(DColor.Night).imePadding()) {
        TopBar(null) { T("Step 1 of 2", DType.Meta.copy(fontWeight = FontWeight.Medium), color = DColor.TextDim, modifier = Modifier.padding(end = 16.dp)) }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(8.dp))
            T("Create your account", DType.H1)
            T("Takes less than a minute. Music stays free.", DType.Body, color = DColor.TextDim, modifier = Modifier.padding(top = 6.dp))
            Spacer(Modifier.height(24.dp))
            DField("Email", email, { email = it })
            Spacer(Modifier.height(16.dp))
            DField("Password", password, { password = it }, placeholder = "Create a password", password = true)
            Row(Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(if (longEnough) Icons.Rounded.CheckCircle else Icons.Rounded.Info, null, tint = if (longEnough) DColor.Success else DColor.TextDim, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                T("12 or more characters", DType.Small, color = if (longEnough) DColor.Success else DColor.TextDim)
            }
            Spacer(Modifier.height(16.dp))
            DField("Display name", name, { name = it }, helper = "Shown on your profile and public playlists. You can change it later.")
            Spacer(Modifier.height(20.dp))
            CheckRow("I agree to the Terms of Use and Privacy Policy", agree) { agree = it }
            CheckRow("Send me new music and updates (optional)", news) { news = it }
            Spacer(Modifier.height(16.dp))
            LinkLine("Already have an account?", "Log in") { nav.go(Route.LogIn) }
            Spacer(Modifier.height(24.dp))
        }
        PrimaryButton("Create account", Modifier.padding(16.dp).navigationBarsPadding(), enabled = valid) { nav.resetTo(Route.Preferences) }
    }
}

@Composable
fun CheckRow(text: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).clickable { onChange(!checked) }.padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.size(22.dp).clip(RoundedCornerShape(6.dp))
                .then(if (checked) Modifier.background(DColor.Marigold) else Modifier.border(1.5.dp, DColor.TextDim, RoundedCornerShape(6.dp))),
            contentAlignment = Alignment.Center,
        ) { if (checked) Icon(Icons.Rounded.Check, null, tint = DColor.Ink, modifier = Modifier.size(16.dp)) }
        Spacer(Modifier.width(12.dp))
        T(text, DType.Meta.copy(fontSize = 14.sp), color = DColor.Text)
    }
}

@Composable
private fun LinkLine(lead: String, link: String, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        T(lead, DType.Label.copy(fontWeight = FontWeight.Medium), color = DColor.TextDim)
        Spacer(Modifier.width(4.dp))
        TextLink(link, onClick = onClick)
    }
}

// ------------------------------------------------------------------ S04 Log in

@Composable
fun LogInScreen() {
    val nav = LocalNav.current
    var email by rememberSaveable { mutableStateOf(Sample.email) }
    var password by rememberSaveable { mutableStateOf("") }
    Column(Modifier.fillMaxSize().background(DColor.Night).imePadding()) {
        TopBar(null)
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(40.dp))
            T("Welcome back", DType.H1)
            T("Log in to pick up where you left off.", DType.Body, color = DColor.TextDim, modifier = Modifier.padding(top = 6.dp))
            Spacer(Modifier.height(24.dp))
            DField("Email", email, { email = it })
            Spacer(Modifier.height(16.dp))
            DField("Password", password, { password = it }, placeholder = "Enter your password", password = true)
            Box(Modifier.fillMaxWidth().padding(top = 8.dp), contentAlignment = Alignment.CenterEnd) {
                TextLink("Forgot password?") { nav.go(Route.ResetPassword) }
            }
            Spacer(Modifier.height(24.dp))
            PrimaryButton("Log in", enabled = password.isNotEmpty()) { nav.resetTo(Route.Home) }
            Row(Modifier.fillMaxWidth().padding(vertical = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.weight(1f).height(1.dp).background(DColor.Line))
                T("or", DType.Meta, color = DColor.TextDim, modifier = Modifier.padding(horizontal = 12.dp))
                Box(Modifier.weight(1f).height(1.dp).background(DColor.Line))
            }
            SecondaryButton("Continue with Google", icon = Icons.Rounded.Language) { nav.resetTo(Route.Home) }
            Spacer(Modifier.height(12.dp))
            SecondaryButton("Continue with phone number", icon = Icons.Rounded.PhoneAndroid) { nav.resetTo(Route.Home) }
            Spacer(Modifier.height(32.dp))
            LinkLine("New here?", "Create an account") { nav.go(Route.SignUp) }
            Spacer(Modifier.height(24.dp))
        }
    }
}

// ------------------------------------------------------------------ S05 Reset password

@Composable
fun ResetPasswordScreen() {
    val nav = LocalNav.current
    var email by rememberSaveable { mutableStateOf(Sample.email) }
    Column(Modifier.fillMaxSize().background(DColor.Night).imePadding()) {
        TopBar(null)
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(80.dp))
            T("Reset your password", DType.H1)
            T("Enter the email you signed up with. We’ll send you a link to set a new password.", DType.Body, color = DColor.TextDim, modifier = Modifier.padding(top = 6.dp))
            Spacer(Modifier.height(24.dp))
            DField("Email", email, { email = it })
            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(DColor.Surface).padding(16.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Icon(Icons.Rounded.Info, null, tint = DColor.Info, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(12.dp))
                T("If an account exists for this email, the link arrives in a few minutes. It works once and then expires.", DType.Meta, color = DColor.TextDim)
            }
            Spacer(Modifier.height(32.dp))
            PrimaryButton("Send reset link", enabled = "@" in email) { nav.say("Reset link sent to $email"); nav.back() }
            Spacer(Modifier.height(16.dp))
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { TextLink("Back to log in", style = DType.BodyStrong) { nav.back() } }
        }
    }
}

// ------------------------------------------------------------------ S06 Listening preferences

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PreferencesScreen() {
    val nav = LocalNav.current
    val langs = listOf("Hindi", "Punjabi", "English", "Tamil", "Telugu", "Bengali", "Marathi", "Malayalam", "Bhojpuri", "Haryanvi")
    val pickedLangs = remember { mutableStateListOf("Hindi", "Punjabi", "English") }
    val artists = listOf(
        Sample.kabirSen, Sample.iraMenon, Sample.nilaRaghavan, Sample.aravKapoor, Sample.mcVeer, Sample.rooftopRadio,
    )
    val pickedArtists = remember { mutableStateListOf("Kabir Sen", "Nila Raghavan", "MC Veer") }
    var query by rememberSaveable { mutableStateOf("") }

    Column(Modifier.fillMaxSize().background(DColor.Night)) {
        TopBar(null) { TextLink("Skip", color = DColor.TextDim, style = DType.BodyStrong, modifier = Modifier.padding(end = 12.dp)) { nav.resetTo(Route.Home) } }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(8.dp))
            T("What do you listen to?", DType.H1)
            T("Pick as many as you like. Your Home is built from these first.", DType.Body, color = DColor.TextDim, modifier = Modifier.padding(top = 6.dp))
            T("LANGUAGES", DType.Overline, color = DColor.TextDim, modifier = Modifier.padding(top = 28.dp, bottom = 12.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                langs.forEach { l ->
                    val on = l in pickedLangs
                    Row(
                        Modifier.height(40.dp).clip(RoundedCornerShape(20.dp))
                            .background(if (on) DColor.Text else DColor.Elevated)
                            .clickable { if (on) pickedLangs.remove(l) else pickedLangs.add(l) }.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (on) { Icon(Icons.Rounded.Check, null, tint = DColor.Night, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)) }
                        T(l, DType.Label, color = if (on) DColor.Night else DColor.Text)
                    }
                }
            }
            T("ARTISTS", DType.Overline, color = DColor.TextDim, modifier = Modifier.padding(top = 32.dp, bottom = 12.dp))
            SearchField(query, { query = it }, "Search artists, films or actors", Icons.Rounded.Search)
            Spacer(Modifier.height(20.dp))
            val shown = artists.filter { query.isBlank() || it.name.contains(query, ignoreCase = true) }
            shown.chunked(3).forEach { row ->
                Row(Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    row.forEach { a ->
                        val on = a.name in pickedArtists
                        Column(
                            Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).clickable { if (on) pickedArtists.remove(a.name) else pickedArtists.add(a.name) },
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Box {
                                ArtImage(a.art, Modifier.size(96.dp).then(if (on) Modifier.border(3.dp, DColor.Marigold, CircleShape) else Modifier), CircleShape)
                                if (on) Box(
                                    Modifier.align(Alignment.BottomEnd).size(28.dp).clip(CircleShape).background(DColor.Marigold),
                                    contentAlignment = Alignment.Center,
                                ) { Icon(Icons.Rounded.Check, null, tint = DColor.Ink, modifier = Modifier.size(18.dp)) }
                            }
                            T(a.name, DType.Meta.copy(fontWeight = FontWeight.SemiBold), modifier = Modifier.padding(top = 8.dp), maxLines = 1)
                        }
                    }
                    repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
        Row(
            Modifier.fillMaxWidth().background(DColor.Surface).navigationBarsPadding().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                T("${pickedLangs.size + pickedArtists.size} picked", DType.BodyStrong)
                T("${pickedLangs.size} languages · ${pickedArtists.size} artists", DType.Small, color = DColor.TextDim)
            }
            PrimaryButton("Continue", Modifier.width(170.dp), enabled = pickedLangs.isNotEmpty()) { nav.resetTo(Route.Home) }
        }
    }
}
