package com.example.klinikcaremobile.features.petugas.register.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.klinikcaremobile.R
import com.example.klinikcaremobile.features.petugas.login.activity.login_petugas

class register_petugas : AppCompatActivity() {
    private lateinit var loginButtonView: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            enableEdgeToEdge()
            setContentView(R.layout.activity_register_petugas)

            loginButtonView = findViewById(R.id.button_login)

            loginButtonView.setOnClickListener{
                navigatetoLoginPage()
            }

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun navigatetoLoginPage() {
        val intent = Intent(this, login_petugas::class.java);
        startActivity(intent);
    }
}