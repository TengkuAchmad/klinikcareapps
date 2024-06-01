package com.example.klinikcaremobile.features.petugas.login.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.klinikcaremobile.R
import com.example.klinikcaremobile.features.petugas.home.activity.home_petugas
import com.example.klinikcaremobile.features.petugas.register.activity.register_petugas

class login_petugas : AppCompatActivity() {

    private lateinit var registerButtonView: Button
    private lateinit var loginButtonView: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            enableEdgeToEdge()
            setContentView(R.layout.activity_login_petugas)

            registerButtonView = findViewById(R.id.button_register)
            loginButtonView    = findViewById(R.id.button_login_request)

            registerButtonView.setOnClickListener{
                navigateToRegisterPage()
            }

            loginButtonView.setOnClickListener{
                navigateToHomePage()
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

    private fun navigateToRegisterPage(){
        val intent  = Intent(this, register_petugas::class.java)
        startActivity(intent)
    }

    private fun navigateToHomePage(){
        val intent  = Intent(this, home_petugas::class.java)
        startActivity(intent)
    }
}