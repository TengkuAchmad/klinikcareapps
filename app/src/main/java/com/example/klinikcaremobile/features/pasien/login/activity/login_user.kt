package com.example.klinikcaremobile.features.pasien.login.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.klinikcaremobile.R
import com.example.klinikcaremobile.api.LoginRequest
import com.example.klinikcaremobile.features.pasien.register.activity.register_user
import com.example.klinikcaremobile.features.pasien.home.activity.home_pasien
import com.example.klinikcaremobile.features.pasien.login.storage.LoginStorage

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
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Email dan password harus diisi", Toast.LENGTH_LONG).show()
                } else {
                    performLoginRequest(email, password)
                }

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

    private fun performLoginRequest(email: String, password: String) {
        val loginStorage = LoginStorage(this)
        val loginRequest = LoginRequest(loginStorage)
        loginRequest.performLoginRequest(email, password) { success, message ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Registrasi Akun berhasil!", Toast.LENGTH_LONG).show()
                    navigateToHomePage()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}