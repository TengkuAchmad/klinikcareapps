package com.example.klinikcaremobile.features.petugas.antrian.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.klinikcaremobile.R
import com.example.klinikcaremobile.features.petugas.login.storage.LoginStorage
import com.example.klinikcaremobile.features.petugas.antrian.api.TicketListRequest
import com.example.klinikcaremobile.features.petugas.antrian.storage.ListAntrianStorage
import com.example.klinikcaremobile.features.petugas.diagnosa.activity.create_diagnosa
import com.example.klinikcaremobile.features.petugas.home.activity.home_petugas

class daftar_pemeriksaan : AppCompatActivity() {
    private lateinit var loginStorage: LoginStorage
    private lateinit var listAntrianStorage: ListAntrianStorage
    private lateinit var queueList: TextView
    private lateinit var buttonDiagnose: Button
    private lateinit var buttonBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daftar_pemeriksaan)

        loginStorage = LoginStorage(this)
        listAntrianStorage = ListAntrianStorage(this)
        queueList = findViewById(R.id.jumlahAntrian)
        buttonDiagnose = findViewById(R.id.button_create_diagnose)
        buttonBack = findViewById(R.id.button_back)

        buttonDiagnose.setOnClickListener{
            val intent = Intent(this, create_diagnosa::class.java)
            startActivity(intent)
        }

        buttonBack.setOnClickListener {
            val intent = Intent(this, home_petugas::class.java)
            startActivity(intent)
        }

        val recyclerview = findViewById<RecyclerView>(R.id.list_checkup_recycler)

        recyclerview.layoutManager = LinearLayoutManager(this)


        val ticketListRequest = TicketListRequest(loginStorage, listAntrianStorage)

        ticketListRequest.getTicketListData { success, message ->
            if (success) {
                val data = ArrayList<CheckupListViewModel>()

                val ticketNumbers = listAntrianStorage.getTicketNumbers()
                val ticketNames = listAntrianStorage.getTicketNames()
                val ticketNiks = listAntrianStorage.getTicketNiks()

                val firstTicketNumber = ticketNumbers.firstOrNull()

                buttonDiagnose.text = if (firstTicketNumber != null) {
                    "BUAT CATATAN MEDIS UNTUK ANTRIAN $firstTicketNumber"
                } else {
                    "TIDAK ADA ANTRIAN TERSISA"
                }

                queueList.text = "${ticketNumbers.size} Antrian"

                for (i in ticketNumbers.indices) {
                    if (ticketNames[i] != null && ticketNiks[i] != null && ticketNumbers[i] != null) {
                        data.add(CheckupListViewModel(ticketNames[i], ticketNiks[i], ticketNumbers[i]))
                    } else {
                        Log.w("daftar_pemeriksaan", "Ignoring null data at index $i")
                    }
                }

                Log.d("daftar_pemeriksaan", "Data: $data")

                val adaptor = CheckUpListAdaptor(data)

                recyclerview.adapter = adaptor
            } else {

                queueList.text = "0 Antrian"

                val data = ArrayList<CheckupListViewModel>()

                data.add(CheckupListViewModel("-", "-", "-"))

                val adaptor = CheckUpListAdaptor(data)

                recyclerview.adapter = adaptor

                Log.e("daftar_pemeriksaan", "Failed to fetch data: $message")
            }
        }

    }
}