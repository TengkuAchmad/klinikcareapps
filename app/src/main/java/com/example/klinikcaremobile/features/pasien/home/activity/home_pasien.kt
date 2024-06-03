package com.example.klinikcaremobile.features.pasien.home.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.klinikcaremobile.R
import com.example.klinikcaremobile.features.pasien.login.storage.IdentityStorage
import com.example.klinikcaremobile.features.pasien.login.storage.TicketStorage
import com.example.klinikcaremobile.features.pasien.profile.activity.profile_user

class home_pasien : AppCompatActivity() {
    private lateinit var buttonProfileView: ImageView
    private lateinit var userNameView: TextView
    private lateinit var nomorAntrianView: TextView
    private lateinit var sisaAntrianView: TextView
    private lateinit var jumlahAntrianView: TextView

    private lateinit var identityStorage: IdentityStorage
    private lateinit var ticketStorage: TicketStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_home_pasien)

        buttonProfileView = findViewById(R.id.avatar)
        userNameView = findViewById(R.id.userName)
        nomorAntrianView = findViewById(R.id.nomor_antrian)
        sisaAntrianView = findViewById(R.id.nomor_antrian_value)
        jumlahAntrianView = findViewById(R.id.jumlah_antrian_value)



        identityStorage = IdentityStorage(this)
        ticketStorage = TicketStorage(this)

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
        val intent = Intent(this, profile_user::class.java)
        startActivity(intent)
    }

    private fun setContent() {
        val fullName = identityStorage.getName() ?: ""
        val firstName = fullName.split(" ").firstOrNull()
        userNameView.text = firstName

        val ticketNumber = ticketStorage.getTicketNumber() ?: ""
        nomorAntrianView.text = ticketNumber

        val remainingTicket = ticketStorage.getRemainingTicket() ?: 0
        val nomorAntrian: String = remainingTicket.toString()
        val formattedNomorAntrian: String = "$nomorAntrian Antrian"
        sisaAntrianView.text = formattedNomorAntrian

        val totalTicket = ticketStorage.getTotalTicket() ?: 0
        val totalTickets: String = totalTicket.toString()
        val formattedTotalTicket: String = "$totalTickets Antrian"
        jumlahAntrianView.text = formattedTotalTicket

    }
}