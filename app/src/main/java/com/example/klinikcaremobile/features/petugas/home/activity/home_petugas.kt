package com.example.klinikcaremobile.features.petugas.home.activity

import TicketOfficerRequest
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.klinikcaremobile.R
import com.example.klinikcaremobile.features.petugas.login.storage.IdentityOfficerStorage
import com.example.klinikcaremobile.features.petugas.login.storage.TicketOfficerStorage
import com.example.klinikcaremobile.features.petugas.profile.activity.profile_petugas

class home_petugas : AppCompatActivity() {
    private lateinit var buttonProfileView: ImageView
    private lateinit var officerNameView: TextView
    private lateinit var waitingTicketView: TextView
    private lateinit var countWaitingTicketView: TextView
    private lateinit var completedTicketView: TextView

    private lateinit var officerIdentityStorage: IdentityOfficerStorage
    private lateinit var officerTicketStorage: TicketOfficerStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_petugas)

        buttonProfileView = findViewById(R.id.avatarOfficer)
        officerNameView = findViewById(R.id.officerName)
        waitingTicketView = findViewById(R.id.nomor_antrian)
        countWaitingTicketView = findViewById(R.id.nomor_antrian_value)
        completedTicketView = findViewById(R.id.jadwal_antrian_value)


        officerIdentityStorage = IdentityOfficerStorage(this)
        officerTicketStorage = TicketOfficerStorage(this)

        setContent()

        buttonProfileView.setOnClickListener{
            NavigateToProfile()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun NavigateToProfile() {
        val intent = Intent(this, profile_petugas::class.java)
        startActivity(intent)
    }

    private fun setContent() {
        val fullName = officerIdentityStorage.getName()?:""
        val firstName = fullName.split(" ").firstOrNull()
        officerNameView.text = firstName

        val waitingTicketNumber = officerTicketStorage.getWaitingTicketNumber() ?: ""
        waitingTicketView.text = waitingTicketNumber

        val countWaitingTicket = officerTicketStorage.getWaitingTotalTicket() ?: ""
        countWaitingTicketView.text = countWaitingTicket.toString()

        val completedTicket = officerTicketStorage.getCompletedTicketNumber() ?:0
        val nomorAntrian: String = completedTicket.toString()
        val formattedNomorAntrian: String = "$nomorAntrian Antrian"
        completedTicketView.text = formattedNomorAntrian
    }
}