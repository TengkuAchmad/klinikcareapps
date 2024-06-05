package com.example.klinikcaremobile.features.petugas.register.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.klinikcaremobile.R
import com.example.klinikcaremobile.api.RegisterOfficerRequest
import com.example.klinikcaremobile.features.petugas.register.responses.successOfficer_registered
import com.example.klinikcaremobile.features.petugas.login.activity.login_petugas
import java.util.Calendar

class register_petugas : AppCompatActivity() {
    private lateinit var loginButtonView: Button
    private lateinit var registerButtonView: Button
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var dateOfBirthEditText: EditText
    private lateinit var nipEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            enableEdgeToEdge()
            setContentView(R.layout.activity_register_petugas)

            loginButtonView = findViewById(R.id.button_login)
            registerButtonView = findViewById(R.id.button_register_petugas)
            nameEditText = findViewById(R.id.name_register_petugas)
            emailEditText = findViewById(R.id.email_register_petugas)
            passwordEditText = findViewById(R.id.password_register_petugas)
            phoneEditText = findViewById(R.id.phone_register_petugas)
            nipEditText = findViewById(R.id.nip_register_petugas)
            dateOfBirthEditText = findViewById(R.id.datebirth_register_petugas)

            dateOfBirthEditText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showDatePickerDialog()
                }
            }

            loginButtonView.setOnClickListener{
                navigatetoLoginPage()
            }

            registerButtonView.setOnClickListener{
                val name = nameEditText.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                val phone = phoneEditText.text.toString()
                val nip = nipEditText.text.toString()
                val birthdate = dateOfBirthEditText.text.toString()

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || nip.isEmpty() || birthdate.isEmpty()) {
                    Toast.makeText(this, "Harap isi data formulir secara lengkap", Toast.LENGTH_LONG).show()
                } else {
                    performRegisterRequest(name, email, password, phone, nip, birthdate)
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

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                dateOfBirthEditText.setText(selectedDate)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun navigatetoLoginPage() {
        val intent = Intent(this, login_petugas::class.java);
        startActivity(intent);
    }

    private fun navigateToSuccessPage() {
        val intent  = Intent(this, successOfficer_registered::class.java)
        startActivity(intent)
    }

    private fun performRegisterRequest(name: String, email: String, password: String, phone: String, nip: String, birthdate: String) {
        val registerOfficerRequest = RegisterOfficerRequest()
        registerOfficerRequest.performRegisterRequest(name, email, password, phone, birthdate, nip) { success, message ->
            runOnUiThread{
                if (success) {
                    navigateToSuccessPage()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}