package com.example.klinikcaremobile.features.pasien.login.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.klinikcaremobile.R
import com.example.klinikcaremobile.features.pasien.register.activity.register_user
import com.example.klinikcaremobile.features.pasien.home.activity.home_pasien

class login_user : AppCompatActivity() {

    private lateinit var registerButtonView: Button
    private lateinit var loginButtonView: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            enableEdgeToEdge()
            setContentView(R.layout.activity_login_user)

            registerButtonView = findViewById(R.id.button_register)
            loginButtonView = findViewById(R.id.button_login_request)
            emailEditText = findViewById(R.id.email_register_user)
            passwordEditText = findViewById(R.id.password_register_user)

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

    private fun navigateToRegisterPage() {
        val intent  = Intent(this, register_user::class.java)
        startActivity(intent)
    }

    private fun navigateToHomePage() {
        val intent = Intent(this, home_pasien::class.java)
        startActivity(intent)
    }
}