package com.dafli.app.platform

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.dafli.app.BuildConfig
import com.dafli.app.player.Media3Engine

/** Android implementation of [Platform]. */
class AndroidPlatform(private val context: Context) : Platform {
    private val prefs = context.getSharedPreferences("dafli", Context.MODE_PRIVATE)

    override val store = object : KeyValueStore {
        override fun getString(key: String): String? = prefs.getString(key, null)
        override fun putString(key: String, value: String?) { prefs.edit().apply { if (value == null) remove(key) else putString(key, value) }.apply() }
    }

    override fun createEngine(events: EngineEvents): PlaybackEngine = Media3Engine(context, events)

    override fun share(title: String, text: String) {
        val send = Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_SUBJECT, title); putExtra(Intent.EXTRA_TEXT, text) }
        start(Intent.createChooser(send, title))
    }

    override fun openUrl(url: String) = start(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

    override fun sendEmail(to: String, subject: String) =
        start(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$to")).putExtra(Intent.EXTRA_SUBJECT, subject))

    override val appVersion: String = BuildConfig.VERSION_NAME

    private fun start(i: Intent) {
        try { context.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) } catch (_: ActivityNotFoundException) { }
    }
}
