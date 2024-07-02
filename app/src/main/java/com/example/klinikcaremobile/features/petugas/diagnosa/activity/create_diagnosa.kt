package com.example.klinikcaremobile.features.petugas.diagnosa.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.klinikcaremobile.R
import com.example.klinikcaremobile.features.pasien.login.storage.IdentityStorage
import com.example.klinikcaremobile.features.petugas.antrian.activity.daftar_pemeriksaan
import com.example.klinikcaremobile.features.petugas.antrian.storage.ListAntrianStorage
import com.example.klinikcaremobile.features.petugas.diagnosa.api.DiagnosaRequest
import com.example.klinikcaremobile.features.petugas.diagnosa.response.success_diagnosa
import com.example.klinikcaremobile.features.petugas.login.api.OfficerDataResponse
import com.example.klinikcaremobile.features.petugas.login.storage.IdentityOfficerStorage
import com.example.klinikcaremobile.features.petugas.login.storage.LoginStorage
import com.example.klinikcaremobile.features.petugas.login.storage.PersonelStorage

class create_diagnosa : AppCompatActivity() {
    private lateinit var buttonBackView: Button
    private lateinit var buttonSaveView: Button
    private lateinit var userNameDiagnoseView: TextView
    private lateinit var userNIKDiagnoseView: TextView
    private lateinit var personelSpinnerView : Spinner
    private lateinit var userDiagnoseView: TextView
    private lateinit var userAlergiView: TextView
    private lateinit var userObatView: TextView

    private lateinit var officerIdentityStorage: IdentityOfficerStorage
    private lateinit var listAntrianStorage: ListAntrianStorage
    private lateinit var loginStorage: LoginStorage
    private lateinit var personelStorage: PersonelStorage

    private var selectedUUID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_diagnosa)

        buttonBackView = findViewById(R.id.button_back)
        buttonSaveView = findViewById(R.id.button_diagnosa_request)
        userNameDiagnoseView = findViewById(R.id.userName_diagnose)
        userNIKDiagnoseView = findViewById(R.id.userNIK_diagnose)
        personelSpinnerView = findViewById(R.id.PersonelSpinner)
        userDiagnoseView = findViewById(R.id.hasil_diagnosa)
        userObatView = findViewById(R.id.hasil_obat_diagnosa)
        userAlergiView = findViewById(R.id.hasil_alergi_diagnosa)

        officerIdentityStorage = IdentityOfficerStorage(this)
        listAntrianStorage = ListAntrianStorage(this)
        loginStorage = LoginStorage(this)
        personelStorage = PersonelStorage(this)

        setContent()

        buttonSaveView.setOnClickListener {
            val nik = userNIKDiagnoseView.text.toString()
            val diagnosa = userDiagnoseView.text.toString()
            val obat = userObatView.text.toString()
            val alergi = userAlergiView.text.toString()

            if (nik.isEmpty() || diagnosa.isEmpty() || obat.isEmpty() || alergi.isEmpty() || selectedUUID == null) {
                Toast.makeText(this, "Harap isi data formulir secara lengkap", Toast.LENGTH_LONG).show()
            } else {
                performCreateDiagnosa(nik, diagnosa, alergi, obat, selectedUUID.toString())
            }
        }


        buttonBackView.setOnClickListener {
            val intent = Intent(this, daftar_pemeriksaan::class.java)
            startActivity(intent)
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setContent() {
        val userNames = listAntrianStorage.getTicketNames()

        val userName = userNames.firstOrNull()
        userNameDiagnoseView.text = userName.toString()

        val userNIKs = listAntrianStorage.getTicketNiks()

        val userNIK = userNIKs.firstOrNull()
        userNIKDiagnoseView.text = userNIK.toString()


        val personelNames = personelStorage.getNamePersonel()
        val personelUuids = personelStorage.getUuidPersonel()


        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, personelNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        personelSpinnerView.adapter = adapter

        personelSpinnerView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedUUID = personelUuids[position]
                Log.d("SelectedPersonel", selectedUUID.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // PASS
            }
        }
    }

    private fun navigateToSuccessPage() {
        val intent = Intent(this, success_diagnosa::class.java)
        startActivity(intent)
    }

    private fun performCreateDiagnosa(nik: String, diagnosa: String, alergi: String, obat:String, id: String) {
        val loginStorage = LoginStorage(this)
        val diagnosaRequest = DiagnosaRequest(loginStorage)
        diagnosaRequest.performCreateDiagnosa(nik, diagnosa, obat, alergi, id) { success, message ->
            runOnUiThread{
                if (success){
                    navigateToSuccessPage()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}