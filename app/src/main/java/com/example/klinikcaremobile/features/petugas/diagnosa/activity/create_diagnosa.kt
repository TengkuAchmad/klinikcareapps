package com.example.klinikcaremobile.features.petugas.diagnosa.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

class create_diagnosa : AppCompatActivity() {
    private lateinit var buttonBackView: Button
    private lateinit var buttonSaveView: Button
    private lateinit var userNameDiagnoseView: TextView
    private lateinit var userNIKDiagnoseView: TextView
    private lateinit var officerNameDiagnoseView: TextView
    private lateinit var userDiagnoseView: TextView
    private lateinit var userAlergiView: TextView
    private lateinit var userObatView: TextView

    private lateinit var officerIdentityStorage: IdentityOfficerStorage
    private lateinit var listAntrianStorage: ListAntrianStorage
    private lateinit var loginStorage: LoginStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_diagnosa)

        buttonBackView = findViewById(R.id.button_back)
        buttonSaveView = findViewById(R.id.button_diagnosa_request)
        officerNameDiagnoseView = findViewById(R.id.officerName_diagnose)
        userNameDiagnoseView = findViewById(R.id.userName_diagnose)
        userNIKDiagnoseView = findViewById(R.id.userNIK_diagnose)
        userDiagnoseView = findViewById(R.id.hasil_diagnosa)
        userObatView = findViewById(R.id.hasil_obat_diagnosa)
        userAlergiView = findViewById(R.id.hasil_alergi_diagnosa)

        officerIdentityStorage = IdentityOfficerStorage(this)
        listAntrianStorage = ListAntrianStorage(this)
        loginStorage = LoginStorage(this)

        setContent()

        buttonSaveView.setOnClickListener {
            val nik = userNIKDiagnoseView.text.toString()
            val diagnosa = userDiagnoseView.text.toString()
            val obat = userObatView.text.toString()
            val alergi = userAlergiView.text.toString()

            if (nik.isEmpty() || diagnosa.isEmpty() || obat.isEmpty() || alergi.isEmpty()) {
                Toast.makeText(this, "Harap isi data formulir secara lengkap", Toast.LENGTH_LONG).show()
            } else {
                performCreateDiagnosa(nik, diagnosa, alergi, obat)
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
        val officerName = officerIdentityStorage.getName() ?:""
        officerNameDiagnoseView.text = officerName.toString()

        Log.d("Name Officer", officerName.toString())

        val userNames = listAntrianStorage.getTicketNames()

        val userName = userNames.firstOrNull()
        userNameDiagnoseView.text = userName.toString()

        Log.d("Name User", userName.toString())

        val userNIKs = listAntrianStorage.getTicketNiks()

        val userNIK = userNIKs.firstOrNull()
        userNIKDiagnoseView.text = userNIK.toString()

        Log.d("NIK User", userNIK.toString())

    }

    private fun navigateToSuccessPage() {
        val intent = Intent(this, success_diagnosa::class.java)
        startActivity(intent)
    }

    private fun performCreateDiagnosa(nik: String, diagnosa: String, alergi: String, obat:String) {
        val loginStorage = LoginStorage(this)
        val diagnosaRequest = DiagnosaRequest(loginStorage)
        diagnosaRequest.performCreateDiagnosa(nik, diagnosa, obat, alergi) { success, message ->
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