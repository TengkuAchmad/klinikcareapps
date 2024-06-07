package com.example.klinikcaremobile.features.pasien.records.activity

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
import com.example.klinikcaremobile.features.pasien.home.activity.home_pasien
import com.example.klinikcaremobile.features.pasien.login.storage.IdentityStorage
import com.example.klinikcaremobile.features.pasien.login.storage.LoginStorage
import com.example.klinikcaremobile.features.pasien.records.api.RecordRequest
import com.example.klinikcaremobile.features.pasien.records.storage.RecordStorage
class hasil_diagnosa : AppCompatActivity() {

    private lateinit var loginStorage: LoginStorage
    private lateinit var recordStorage: RecordStorage
    private lateinit var identityStorage: IdentityStorage

    private lateinit var countRecordView: TextView
    private lateinit var userNameView: TextView
    private lateinit var buttonBackView: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hasil_diagnosa)

        loginStorage = LoginStorage(this)
        recordStorage = RecordStorage(this)
        identityStorage = IdentityStorage(this)

        userNameView = findViewById(R.id.userNameView_Diagnosa)
        countRecordView = findViewById(R.id.jumlahRecords)
        buttonBackView = findViewById(R.id.button_back_diagnosa)

        setContent()

        buttonBackView.setOnClickListener {
            val intent = Intent(this, home_pasien::class.java)
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.list_records_recycler)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val recordRequest = RecordRequest(loginStorage, recordStorage)

        recordRequest.getRecordList { success,message ->
            if (success) {
                val data = ArrayList<RecordListViewModel>()

                val diagnoses = recordStorage.getRecordDiagnose()
                Log.d("Data", diagnoses.toString())
                val alergies = recordStorage.getRecordAlergi()
                val obats = recordStorage.getRecordObat()
                val times = recordStorage.getRecordTime()

                countRecordView.text = "${diagnoses.size} RIWAYAT PEMERIKSAAN"

                for (i in diagnoses.indices) {
                    if (diagnoses[i] != null && alergies[i] != null && obats[i] != null && times[i] != null){
                        data.add(RecordListViewModel(diagnoses[i], alergies[i], obats[i], times[i]))
                    } else {
                        Log.w("Daftar_Records", "Ignoring null data at index $i")
                    }
                }

                Log.d("Daftar_Records", "Data: $data")

                val adaptor = RecordsAdaptor(data)

                recyclerView.adapter = adaptor
            } else {
                val data = ArrayList<RecordListViewModel>()

                data.add(RecordListViewModel("-", "-", "-", "-"))

                val adaptor = RecordsAdaptor(data)

                recyclerView.adapter = adaptor

                Log.e("Daftar_Records", "Failed to fetch data")
            }
        }
    }

    private fun setContent(){
        userNameView.text = identityStorage.getName()
    }
}