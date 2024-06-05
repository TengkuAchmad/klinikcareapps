package com.example.klinikcaremobile.features.pasien.profile.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.klinikcaremobile.R
import com.example.klinikcaremobile.features.pasien.home.activity.home_pasien
import com.example.klinikcaremobile.features.pasien.login.storage.IdentityStorage
import com.example.klinikcaremobile.features.roles.Roles

class profile_user : AppCompatActivity() {
    private lateinit var buttonHomeView: Button
    private lateinit var userNameProfileView: TextView
    private lateinit var identityStorage: IdentityStorage
    private lateinit var cardExitView : CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_pasien)

        buttonHomeView = findViewById(R.id.buttonUser_Home)
        userNameProfileView = findViewById(R.id.UserNameProfile)
        cardExitView  = findViewById(R.id.card_exit)

        identityStorage = IdentityStorage(this)

        setContent()

        cardExitView.setOnClickListener {
            navigateExit()
        }

        buttonHomeView.setOnClickListener {
            navigateToHome()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, home_pasien::class.java)
        startActivity(intent)
    }

    private fun navigateExit() {
        clearStorage()
        val intent = Intent(this, Roles::class.java)
        startActivity(intent)
    }

    private fun clearStorage() {
        val sharedPreferences = getSharedPreferences("storage", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear().apply()
    }

    private fun setContent() {
        val userName = identityStorage.getName()
        userNameProfileView.text = userName
    }
}