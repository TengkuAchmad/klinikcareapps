package com.example.klinikcaremobile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.klinikcaremobile.features.roles.Roles


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        // Starting the Roles activity
        val intent = Intent(this, Roles::class.java)
        startActivity(intent)
        finish()
    }
}
