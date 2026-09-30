package com.dafli.app

/** One place for the things you will want to change before publishing. */
object AppConfig {
    /** Shown in Help and used for "Report" emails. Play Console also asks for a contact email. */
    const val SUPPORT_EMAIL = "jhas60488@gmail.com"

    /** Public privacy-policy URL (the same one you paste into Play Console). */
    const val PRIVACY_URL = "https://dafli-music.github.io/privacy"

    /** Where the music comes from (required attribution). */
    const val MUSIC_SOURCE = "JioSaavn (community API)"
    const val MUSIC_SOURCE_URL = "https://www.jiosaavn.com"
    /** Compatible self-hosted instances can replace this URL. Never embed secrets here. */
    const val SAAVN_API_URL = "https://saavn.sumit.co/api"
}
