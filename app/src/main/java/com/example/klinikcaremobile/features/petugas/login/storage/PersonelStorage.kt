package com.example.klinikcaremobile.features.petugas.login.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PersonelStorage(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "storage",
        Context.MODE_PRIVATE
    )

    companion object {
        const val PERSONEL_UUID_LIST = "uuidPersonelList"
        const val PERSONEL_NAME_LIST = "namePersonelList"
        const val PERSONEL_ROLE_LIST = "rolePersonelList"
    }
    fun saveUuidPersonel(uuidPersonel: List<String>) {
        val json = Gson().toJson(uuidPersonel)
        sharedPreferences.edit().putString(PERSONEL_UUID_LIST, json).apply()
    }

    fun saveNamePersonel(namePersonel: List<String>){
        val json = Gson().toJson(namePersonel)
        sharedPreferences.edit().putString(PERSONEL_NAME_LIST, json).apply()
    }

    fun saveRolePersonel(rolePersonel: List<String>){
        val json = Gson().toJson(rolePersonel)
        sharedPreferences.edit().putString(PERSONEL_ROLE_LIST, json).apply()
    }

    fun getUuidPersonel(): List<String> {
        val json = sharedPreferences.getString(PERSONEL_UUID_LIST, "")
        return Gson().fromJson(json, object: TypeToken<List<String>>() {}.type) ?: listOf()
    }

    fun getNamePersonel(): List<String> {
        val json = sharedPreferences.getString(PERSONEL_NAME_LIST, "")
        return Gson().fromJson(json, object: TypeToken<List<String>>() {}.type) ?: listOf()
    }

    fun getRolePersonel(): List<String> {
        val json = sharedPreferences.getString(PERSONEL_ROLE_LIST, "")
        return Gson().fromJson(json, object: TypeToken<List<String>>() {}.type) ?: listOf()
    }
}