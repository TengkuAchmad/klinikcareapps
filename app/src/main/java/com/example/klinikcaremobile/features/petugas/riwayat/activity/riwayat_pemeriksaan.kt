package com.example.klinikcaremobile.features.petugas.riwayat.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.klinikcaremobile.R
import com.example.klinikcaremobile.features.petugas.home.activity.home_petugas
import com.example.klinikcaremobile.features.petugas.login.storage.LoginStorage
import com.example.klinikcaremobile.features.petugas.riwayat.api.RiwayatRequest
import com.example.klinikcaremobile.features.petugas.riwayat.storage.RiwayatStorage

class riwayat_pemeriksaan : AppCompatActivity() {
    private lateinit var loginStorage: LoginStorage
    private lateinit var riwayatStorage: RiwayatStorage


    private lateinit var buttonBack: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_riwayat_pemeriksaan)

        loginStorage = LoginStorage(this)
        riwayatStorage = RiwayatStorage(this)

        buttonBack = findViewById(R.id.button_back)

        buttonBack.setOnClickListener {
            val intent = Intent(this, home_petugas::class.java)
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.list_riwayat_recycler)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val riwayatRequest = RiwayatRequest(loginStorage, riwayatStorage)

        riwayatRequest.getRiwayatListData { success, message ->
            if (success) {
                val data = ArrayList<RiwayatListViewModel>()

                val IdNumbers = riwayatStorage.getRiwayatID()
                val userNames = riwayatStorage.getRiwayatName()

                for (i in IdNumbers.indices ){
                    if (IdNumbers[i] != null && userNames[i] != null){
                        data.add(RiwayatListViewModel(IdNumbers[i], userNames[i]))
                    } else {
                        Log.w("daftar_riwayat", "Ignoring null data at index $i")
                    }
                }

                Log.d("daftar_riwayat", "Data: $data")

                val adaptor = RiwayatListAdaptor(data)

                recyclerView.adapter = adaptor
            } else {
                val data = ArrayList<RiwayatListViewModel>()

                data.add(RiwayatListViewModel("-", "Tidak ada catatan pemeriksaan"))

                val adaptor = RiwayatListAdaptor(data)

                recyclerView.adapter = adaptor

                Log.e("daftar_riwayat", "Failed to fetch data: $message")
            }
        }

    }
}