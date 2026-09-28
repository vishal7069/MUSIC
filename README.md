# Dafli — Android app (Kotlin + Jetpack Compose)

> **v1 (Play Store build):** real music from the free Audius API, background playback (Media3), and likes/history saved on the phone. Steps to go live are in **PLAYSTORE_GUIDE.md**. Store text, icon, feature graphic and privacy page are in **store/**.

*Music, sabka.* This is the working Android build of the Dafli case study from Figma. It has every screen (S01–S54, minus the podcast and audiobook screens we removed), the splash animation, and a player you can click through.

## Run it (Android Studio)

1. Unzip the project, then in Android Studio choose **File → Open** and pick the `Dafli` folder.
2. Let Gradle sync. The first sync downloads Android SDK 36, AGP 8.13 and the libraries, so it needs internet.
3. Pick an emulator or phone (Android 8.0 / API 26 or newer) and press **▶ Run**.

To build from the command line, run `./gradlew assembleDebug`. The APK ends up in `app/build/outputs/apk/debug/`.

## What's inside

| Folder | What it holds |
|---|---|
| `theme/` | Design tokens from Figma: Night, Surface, Elevated, Line, Marigold, Ink, status colours, and the Inter type scale |
| `ui/brand/` | The **d** mark (drawn from the same SVG path as the Figma component), the `dafli` lockup, and the **Splash** screen. The splash ports the 3-second Figma Motion timeline keyframe by keyframe: it grows from the icon, hits "Da", hits "fli", the d glides left, "aflı" comes in, the jingle drops, then the tagline. |
| `ui/components/` | Buttons, chips, fields, rows, cards, the mini-player and the bottom nav |
| `ui/screens/` | All the screens (see the list below) plus the bottom sheets |
| `nav/` | A small back-stack navigator. The route names match the S-numbers in Figma. |
| `player/` | Player state (queue, shuffle, repeat, sleep timer) + `Media3Engine` / `PlaybackService` for real audio |
| `data/remote/` | `AudiusApi`: trending, search, artists, playlists, stream URLs |
| `data/Repository.kt` | `MusicRepository` interface, with `AudiusRepository` (live) and `SampleRepository` (previews) |
| `data/Library.kt` | Likes, history, searches and taste, saved in SharedPreferences |
| `AppConfig.kt` | Support email, privacy URL, music source |
| `data/SampleData.kt` | Fictional sample catalogue used only for previews and screenshots |
| `res/drawable-nodpi` | The case study artwork, generated for Dafli |
| `docs/previews` | Screenshots rendered from this exact code |

## Screens

- **Onboarding:** S01 Splash · S02 Welcome · S03 Sign up · S04 Log in · S05 Reset password · S06 Preferences
- **Home:** S07 Home · S08 Made for you · S09 Updates · S10 Recently played
- **Search:** S11 Search · S12 Typing · S13 Results · S14 Genre/Language · S54 Voice search
- **Detail pages:** S15 Artist · S16 Album · S17 Playlist (collaborative)
- **Player:** S19 Now Playing · S20 Queue · S21 Lyrics (synced) · S52 Music video
- **Sheets:** S22 Track menu · S30 Output device · S31 Sleep timer · S41 Share · S40 Report
- **Library:** S23 Library · S24 Liked Songs · S25 Create playlist · S26 Edit · S27 Collaborators · S28 Folder · S29 Downloads · S43 Local files
- **Profile and settings:** S32 My profile · S33 Other profile · S34 Settings · S35 Playback · S36 Audio & storage · S37 Privacy · S38 Account · S39 Help
- **Social:** S42 Listen Together · S44 Shared mix · S45 Recap · S46 Create with AI · S47 Messages · S48 Chat · S53 Your taste

## Behaviour notes

- The mini-player and bottom nav stay **fixed** while content scrolls, the same as in the Figma prototype.
- The v1 Welcome screen has one button, **Get started**. There is no fake login, because the app works without an account.
- Screens that need a backend (login, chat, Listen Together, downloads, lyrics, video) are kept in code for later but are not reachable in v1.
- The splash has a system splash (Android 12 SplashScreen API) showing the d on Night. The Compose animation then continues from that same frame. Tap anywhere to skip.
