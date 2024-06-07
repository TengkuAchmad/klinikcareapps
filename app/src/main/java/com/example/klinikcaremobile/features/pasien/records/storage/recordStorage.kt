package com.example.klinikcaremobile.features.pasien.records.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Locale

class RecordStorage(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "storage",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val RECORD_DIAGNOSE = "recordDiagnoseData"
        private const val RECORD_ALERGI = "recordAlergiData"
        private const val RECORD_OBAT = "recordObatData"
        private const val RECORD_TIME = "recordTimeData"
    }

    fun saveRecordDiagnose(diagnoses : List<String>){
        val json = Gson().toJson(diagnoses)
        sharedPreferences.edit().putString(RECORD_DIAGNOSE, json).apply()
    }

    fun saveRecordAlergi(Alergis : List<String>){
        val json = Gson().toJson(Alergis)
        sharedPreferences.edit().putString(RECORD_ALERGI, json).apply()
    }
    fun saveRecordObat(Obats : List<String>){
        val json = Gson().toJson(Obats)
        sharedPreferences.edit().putString(RECORD_OBAT, json).apply()
    }
    fun saveRecordTime(times: List<String>) {
        val formattedTimes = times.map { originalTime ->
            try {
                val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val targetFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = originalFormat.parse(originalTime)
                if (date != null) {
                    targetFormat.format(date)
                } else {
                    originalTime
                }
            } catch (e: Exception) {
                originalTime
            }
        }
        val json = Gson().toJson(formattedTimes)
        sharedPreferences.edit().putString(RECORD_TIME, json).apply()
    }

    fun getRecordDiagnose() : List<String> {
        val json = sharedPreferences.getString(RECORD_DIAGNOSE, "")
        return Gson().fromJson(json, object : TypeToken<List<String>>() {}.type) ?: listOf()
    }

    fun getRecordAlergi() : List<String> {
        val json = sharedPreferences.getString(RECORD_ALERGI, "")
        return Gson().fromJson(json, object : TypeToken<List<String>>() {}.type) ?: listOf()
    }

    fun getRecordObat() : List<String> {
        val json = sharedPreferences.getString(RECORD_OBAT, "")
        return Gson().fromJson(json, object : TypeToken<List<String>>() {}.type) ?: listOf()
    }
    fun getRecordTime() : List<String> {
        val json = sharedPreferences.getString(RECORD_TIME, "")
        return Gson().fromJson(json, object : TypeToken<List<String>>() {}.type) ?: listOf()
    }

}