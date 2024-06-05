package com.example.klinikcaremobile.features.petugas.profile.activity

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
import com.example.klinikcaremobile.features.petugas.home.activity.home_petugas
import com.example.klinikcaremobile.features.petugas.login.storage.IdentityOfficerStorage
import com.example.klinikcaremobile.features.roles.Roles

class profile_petugas : AppCompatActivity() {
    private lateinit var buttonHomeView: Button
    private lateinit var userNameView: TextView
    private lateinit var buttonExitView: CardView

    private lateinit var identityOfficerStorage: IdentityOfficerStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_petugas)
        buttonHomeView = findViewById(R.id.buttonOfficer_Home)
        userNameView = findViewById(R.id.UserNameProfile)
        buttonExitView = findViewById(R.id.card_exit)

        identityOfficerStorage = IdentityOfficerStorage(this)

        setContent()


        buttonExitView.setOnClickListener{
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
        val intent = Intent(this, home_petugas::class.java)
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
        val identityOfficerStorage = IdentityOfficerStorage(this)
        val userName = identityOfficerStorage.getName().toString() ?: ""

        userNameView.text = userName
    }
}