package com.example.klinikcaremobile.features.petugas.login.activity

import TicketOfficerRequest
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
import com.example.klinikcaremobile.features.petugas.home.activity.home_petugas
import com.example.klinikcaremobile.features.petugas.login.api.IdentityRequest
import com.example.klinikcaremobile.features.petugas.login.api.LoginRequest
import com.example.klinikcaremobile.features.petugas.login.storage.IdentityOfficerStorage
import com.example.klinikcaremobile.features.petugas.login.storage.LoginStorage
import com.example.klinikcaremobile.features.petugas.login.storage.TicketOfficerStorage
import com.example.klinikcaremobile.features.petugas.register.activity.register_petugas

class login_petugas : AppCompatActivity() {

    private lateinit var registerButtonView: Button
    private lateinit var loginButtonView: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            enableEdgeToEdge()
            setContentView(R.layout.activity_login_petugas)

            registerButtonView  = findViewById(R.id.button_register)
            loginButtonView     = findViewById(R.id.button_login_request)
            emailEditText       = findViewById(R.id.email_login_petugas)
            passwordEditText    = findViewById(R.id.password_login_petugas)

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

    private fun navigateToRegisterPage(){
        val intent  = Intent(this, register_petugas::class.java)
        startActivity(intent)
    }

    private fun navigateToHomePage(){
        val intent  = Intent(this, home_petugas::class.java)
        startActivity(intent)
    }

    private fun performLoginRequest(email: String, password: String) {
        val loginStorage = LoginStorage(this)
        val identityOfficerStorage = IdentityOfficerStorage(this)
        val ticketOfficerStorage = TicketOfficerStorage(this)

        val loginRequest = LoginRequest(loginStorage)
        val identityRequest = IdentityRequest(loginStorage, identityOfficerStorage)
        val ticketRequest = TicketOfficerRequest(loginStorage, ticketOfficerStorage)

        loginRequest.performLoginRequest(email, password) { success, message ->
            runOnUiThread {
                if (success) {
                    identityRequest.getOfficerData { success, message ->
                        if (success) {
                        ticketRequest.getTicketOfficerData { success, message ->
                            if (success){
                                navigateToHomePage()
                            } else {
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}