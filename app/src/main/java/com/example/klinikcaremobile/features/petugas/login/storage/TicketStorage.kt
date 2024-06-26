package com.example.klinikcaremobile.features.petugas.login.storage

import android.content.Context
import android.content.SharedPreferences

class TicketOfficerStorage(context: Context){
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "storage",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val TICKET_NUMBER_KEY = "waitingTicket"
        private const val TOTAL_TICKET_KEY = "totalWaitingTicket"
        private const val COMPLETED_TOTAL_TICKET_KEY = "totalCompletedTicket"
    }

    fun saveCompletedTicketNumber(completedTicket: Int){
        sharedPreferences.edit().putInt(COMPLETED_TOTAL_TICKET_KEY, completedTicket).apply()
    }

    fun getCompletedTicketNumber(): Int? {
        return sharedPreferences.getInt(COMPLETED_TOTAL_TICKET_KEY, 0)
    }


    fun saveWaitingTicketNumber(waitingTicket: Int){
        sharedPreferences.edit().putInt(TICKET_NUMBER_KEY, waitingTicket).apply()
    }

    fun getWaitingTicketNumber(): Int? {
        return sharedPreferences.getInt(TICKET_NUMBER_KEY, 0)
    }

    fun saveTotalWaitingTicket(waitingticketTotal: Int){
        sharedPreferences.edit().putInt(TOTAL_TICKET_KEY, waitingticketTotal).apply()
    }

    fun getWaitingTotalTicket(): Int? {
        return sharedPreferences.getInt(TOTAL_TICKET_KEY, 0)
    }

}