package com.example.klinikcaremobile.features.petugas.antrian.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.klinikcaremobile.R

class daftar_pemeriksaan : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daftar_pemeriksaan)

        val recyclerview = findViewById<RecyclerView>(R.id.list_checkup_recycler)

        recyclerview.layoutManager = LinearLayoutManager(this)

        val data = ArrayList<CheckupListViewModel>()

//        ADDING DATA
        data.add(CheckupListViewModel("Santa Silalahi", "90124124", "001"))
        data.add(CheckupListViewModel("Tengku Achmad", "1024124", "002"))
        data.add(CheckupListViewModel("Risky Pranata", "91234124", "003"))

        val adaptor = CheckUpListAdaptor(data)

        recyclerview.adapter = adaptor

    }
}