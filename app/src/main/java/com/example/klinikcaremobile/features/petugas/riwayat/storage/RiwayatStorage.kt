package com.example.klinikcaremobile.features.petugas.riwayat.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RiwayatStorage(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "storage",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val RIWAYAT_ID_NUMBER = "riwayatIdNumber"
        private const val RIWAYAT_NAME = "riwayatName"
    }

    fun saveRiwayatID(riwayatIdNumbers: List<String>) {
        val json = Gson().toJson(riwayatIdNumbers)
        sharedPreferences.edit().putString(RIWAYAT_ID_NUMBER, json).apply()
    }

    fun saveRiwayatName(riwayatName: List<String>){
        val json = Gson().toJson((riwayatName))
        sharedPreferences.edit().putString(RIWAYAT_NAME, json).apply()
    }

    fun getRiwayatID() : List<String> {
        val json = sharedPreferences.getString(RIWAYAT_ID_NUMBER, "")
        return Gson().fromJson(json, object : TypeToken<List<String>>() {}.type) ?: listOf()
    }

    fun getRiwayatName() : List<String> {
        val json = sharedPreferences.getString(RIWAYAT_NAME, "")
        return Gson().fromJson(json, object : TypeToken<List<String>>() {}.type) ?: listOf()
    }

}