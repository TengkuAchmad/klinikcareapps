package com.example.klinikcaremobile.features.pasien.appointment.storage

import android.content.Context
import android.content.SharedPreferences

class TicketInfoStorage(context: Context){
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "storage",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val TICKET_NUMBER_KEY = "numberTicket"
        private const val DATETIME_TICKET_KEY = "dateTicket"
    }

    fun saveTicketNumber(ticketNumber: String){
        sharedPreferences.edit().putString(TICKET_NUMBER_KEY, ticketNumber).apply()
    }

    fun getTicketNumber(): String? {
        return sharedPreferences.getString(TICKET_NUMBER_KEY, null)
    }

    fun saveDatetimeTicket(Date: String, Time: String){
        val DateData = Date
        val TimeData = Time

        val formattedDatetimeData = DateData + " - " + TimeData + " WIB"
        sharedPreferences.edit().putString(DATETIME_TICKET_KEY, formattedDatetimeData).apply()
    }

    fun getDatetimeTicket(): String? {
        return sharedPreferences.getString(DATETIME_TICKET_KEY, "")
    }


}