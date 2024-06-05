package com.example.klinikcaremobile.features.pasien.login.storage

import android.content.Context
import android.content.SharedPreferences

class TicketStorage(context: Context){
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "storage",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val TICKET_NUMBER_KEY = "numberTicket"
        private const val TOTAL_TICKET_KEY = "totalTicket"
        private const val REMAINING_TICKET_KEY = "remainingTicket"
        private const val REMAINING_QUEUE_TICKET_KEY = "remainingqueueTicket"
    }

    fun saveTicketNumber(ticketNumber: String){
        sharedPreferences.edit().putString(TICKET_NUMBER_KEY, ticketNumber).apply()
    }

    fun getTicketNumber(): String? {
        return sharedPreferences.getString(TICKET_NUMBER_KEY, null)
    }

    fun saveTotalTicket(ticketTotal: Int){
        sharedPreferences.edit().putInt(TOTAL_TICKET_KEY, ticketTotal).apply()
    }

    fun getTotalTicket(): Int? {
        return sharedPreferences.getInt(TOTAL_TICKET_KEY, 0)
    }

    fun saveRemainingTicket(ticketRemaining: Int){
        sharedPreferences.edit().putInt(REMAINING_TICKET_KEY, ticketRemaining).apply()
    }

    fun getRemainingTicket(): Int? {
        return sharedPreferences.getInt(REMAINING_TICKET_KEY, 0)
    }

    fun saveRemainingQueueTicket(ticketQueue: String){
        sharedPreferences.edit().putString(REMAINING_QUEUE_TICKET_KEY, ticketQueue).apply()
    }

    fun getRemainingQueueTicket(): String? {
        return sharedPreferences.getString(REMAINING_QUEUE_TICKET_KEY, null)
    }

}