package com.example.klinikcaremobile.features.pasien.appointment.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.klinikcaremobile.R
import com.example.klinikcaremobile.features.pasien.home.activity.home_pasien

class ticket_cancel_success : AppCompatActivity() {

    private lateinit var buttonBackView: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ticket_cancel_success)

        buttonBackView = findViewById(R.id.button_back)

        buttonBackView.setOnClickListener {
            val intent = Intent(this, home_pasien::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}