package com.example.klinikcaremobile.features.pasien.appointment.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.klinikcaremobile.R
import com.example.klinikcaremobile.features.pasien.appointment.api.AppointmentRequest
import com.example.klinikcaremobile.features.pasien.appointment.api.TicketInfoRequest
import com.example.klinikcaremobile.features.pasien.appointment.storage.TicketInfoStorage
import com.example.klinikcaremobile.features.pasien.home.activity.home_pasien
import com.example.klinikcaremobile.features.pasien.login.storage.IdentityStorage
import com.example.klinikcaremobile.features.pasien.login.storage.LoginStorage
import java.util.Calendar

class appointment : AppCompatActivity() {

    private lateinit var tanggalAppointmentView: EditText
    private lateinit var buttonBackAppointmentView: Button
    private lateinit var buttonAppointmentView: Button
    private lateinit var waktuAppointmentView: EditText
    private lateinit var userNameView: TextView
    private lateinit var nikView: TextView

    private lateinit var identityStorage: IdentityStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_appointment)

        userNameView = findViewById(R.id.userName_appointment)
        nikView = findViewById(R.id.nik_appointment)
        tanggalAppointmentView = findViewById(R.id.tanggal_appointment)
        buttonBackAppointmentView = findViewById(R.id.back_appointment)
        waktuAppointmentView = findViewById(R.id.waktu_appointment)
        buttonAppointmentView = findViewById(R.id.button_appointment)

        identityStorage = IdentityStorage(this)

        setContent()

        tanggalAppointmentView.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePickerDialog()
            }
        }

        tanggalAppointmentView.setOnClickListener{
            showTimePickerDialog()
        }

        waktuAppointmentView.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showTimePickerDialog()
            }
        }

        waktuAppointmentView.setOnClickListener{
            showTimePickerDialog()
        }

        buttonBackAppointmentView.setOnClickListener {
            val intent = Intent(this, home_pasien::class.java)
            startActivity(intent)
        }

        buttonAppointmentView.setOnClickListener {
            val tanggal = tanggalAppointmentView.text.toString()
            val waktu = waktuAppointmentView.text.toString()

            if (tanggal.isEmpty() || waktu.isEmpty()) {
                Toast.makeText(this, "Tanggal dan waktu harus diisi", Toast.LENGTH_LONG).show()
            } else {
                performAppointmentRequest(tanggal, waktu)
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                tanggalAppointmentView.setText(selectedDate)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _: TimePicker?, selectedHour: Int, selectedMinute: Int ->
                if (selectedHour in 8..16) {
                    val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    waktuAppointmentView.setText(formattedTime)
                } else if (selectedHour == 17 && selectedMinute == 0) {
                    val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    waktuAppointmentView.setText(formattedTime)
                } else {
                    waktuAppointmentView.setText("")
                }
            },
            if (hour in 8..16) hour else 8,
            minute,
            true
        )
        timePickerDialog.show()
    }

    private fun setContent() {
        val userName = identityStorage.getName()?:""
        userNameView.text = userName

        val nik = identityStorage.getNik()?:""
        nikView.text = nik
    }

    private fun navigateToTicketPage(){
        val intent = Intent(this, ticket_info::class.java)
        startActivity(intent)
    }

    private fun performAppointmentRequest(tanggal: String, waktu:String){
        val loginStorage = LoginStorage(this)
        val appointmentRequest = AppointmentRequest(loginStorage)
        val ticketInfoStorage = TicketInfoStorage(this)
        val identityStorage = IdentityStorage(this)
        val ticketInfoRequest = TicketInfoRequest(loginStorage,identityStorage, ticketInfoStorage )

        appointmentRequest.performAppointmentRequest(tanggal, waktu) { success, message ->
            runOnUiThread{
                if (success){
                    ticketInfoRequest.getTicketInfoData { success, message ->
                        if (success) {
                            navigateToTicketPage()
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}