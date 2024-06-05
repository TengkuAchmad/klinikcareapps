package com.example.klinikcaremobile.features.pasien.appointment.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.klinikcaremobile.R
import com.example.klinikcaremobile.features.pasien.appointment.api.AppointmentCancelRequest
import com.example.klinikcaremobile.features.pasien.appointment.storage.TicketInfoStorage
import com.example.klinikcaremobile.features.pasien.home.activity.home_pasien
import com.example.klinikcaremobile.features.pasien.login.storage.IdentityStorage
import com.example.klinikcaremobile.features.pasien.login.storage.LoginStorage
import com.example.klinikcaremobile.features.pasien.login.storage.TicketStorage

class ticket_info : AppCompatActivity() {

    private lateinit var textValueTicketView: TextView
    private lateinit var textNameValueTicketView: TextView
    private lateinit var textDateTimeValueTicketView: TextView
    private lateinit var buttonBackTicketInfo: Button
    private lateinit var buttonCancelTicketInfo: Button

    private lateinit var identityStorage: IdentityStorage
    private lateinit var ticketInfoStorage: TicketInfoStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ticket_info)

        textValueTicketView = findViewById(R.id.textValueTicket)
        textNameValueTicketView = findViewById(R.id.textNameValueTicket)
        textDateTimeValueTicketView = findViewById(R.id.textDatetimeValueTicket)
        buttonBackTicketInfo = findViewById(R.id.button_back_ticketInfo)
        buttonCancelTicketInfo = findViewById(R.id.cancel_button_ticket)

        identityStorage = IdentityStorage(this)
        ticketInfoStorage = TicketInfoStorage(this)


        setContent()

        buttonCancelTicketInfo.setOnClickListener {
            performTicketCancel()
        }

        buttonBackTicketInfo.setOnClickListener {
            val intent = Intent(this, home_pasien::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setContent() {
        val TicketNumber = ticketInfoStorage.getTicketNumber()
        val TicketDatetime = ticketInfoStorage.getDatetimeTicket()
        val userName = identityStorage.getName()

        textValueTicketView.text = TicketNumber
        textDateTimeValueTicketView.text = TicketDatetime
        textNameValueTicketView.text = userName

    }

    private fun navigateToCancelSuccess() {
        val intent = Intent(this, ticket_cancel_success::class.java )
        startActivity(intent)
    }

    private fun performTicketCancel() {
        val identityStorage = IdentityStorage(this)
        val loginStorage = LoginStorage(this)
        val appointmentCancelRequest = AppointmentCancelRequest(loginStorage, identityStorage)

        appointmentCancelRequest.performAppointmentCancelRequest() { success, message ->
            runOnUiThread {
                if (success) {
                    navigateToCancelSuccess()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}