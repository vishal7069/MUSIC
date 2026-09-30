package com.dafli.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dafli.app.data.SaavnRepository
import com.dafli.app.platform.AndroidPlatform
import com.dafli.app.theme.DafliTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // System splash shows the d on Night; our Compose splash (S01) continues from the same frame.
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        super.onCreate(savedInstanceState)
        val platform = AndroidPlatform(applicationContext)
        val repo = SaavnRepository()
        setContent { DafliTheme { DafliApp(platform, repo) } }
    }
}
