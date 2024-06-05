package com.example.klinikcaremobile.features.petugas.antrian.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListAntrianStorage(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "storage",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val TICKET_NUMBER_WAITING_LIST = "numberTicketWaitingList"
        private const val TICKET_NAME_WAITING_LIST = "nameTicketWaitingList"
        private const val TICKET_NIK_WAITING_LIST = "nikTicketWaitingList"
    }
    fun saveTicketNumbers(ticketNumbers: List<String>) {
        val json = Gson().toJson(ticketNumbers)
        sharedPreferences.edit().putString(TICKET_NUMBER_WAITING_LIST, json).apply()
    }

    fun saveTicketNames(userNames: List<String?>){
        val json = Gson().toJson(userNames)
        sharedPreferences.edit().putString(TICKET_NAME_WAITING_LIST, json).apply()
    }

    fun saveTicketNiks(userNiks: List<String?>){
        val json = Gson().toJson(userNiks)
        sharedPreferences.edit().putString(TICKET_NIK_WAITING_LIST, json).apply()
    }

    fun getTicketNumbers(): List<String> {
        val json = sharedPreferences.getString(TICKET_NUMBER_WAITING_LIST, "")
        return Gson().fromJson(json, object : TypeToken<List<String>>() {}.type) ?: listOf()
    }

    fun getTicketNames(): List<String> {
        val json = sharedPreferences.getString(TICKET_NAME_WAITING_LIST, "")
        return Gson().fromJson(json, object : TypeToken<List<String>>() {}.type) ?: listOf()
    }

    fun getTicketNiks(): List<String> {
        val json = sharedPreferences.getString(TICKET_NIK_WAITING_LIST, "")
        return Gson().fromJson(json, object : TypeToken<List<String>>() {}.type) ?: listOf()
    }
}