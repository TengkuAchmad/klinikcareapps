package com.example.klinikcaremobile.features.pasien.login.storage

import android.content.Context
import android.content.SharedPreferences

class IdentityStorage(context: Context){
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "storage",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val UUID_KEY = "uuidUser"
        private const val EMAIL_KEY = "emailUser"
        private const val NAME_KEY = "nameUser"
        private const val PHONE_KEY = "phoneUser"
        private const val PHOTO_KEY = "photoUser"
        private const val NIK_KEY = "nikUser"
        private const val BIRTHDATE_KEY = "birthdateUser"
    }

    fun saveUUID(uuid: String){
        sharedPreferences.edit().putString(UUID_KEY, uuid).apply()
    }

    fun getUUID(): String? {
        return sharedPreferences.getString(UUID_KEY, null)
    }

    fun saveEmail(email: String){
        sharedPreferences.edit().putString(EMAIL_KEY, email).apply()
    }

    fun getEmail(): String? {
        return sharedPreferences.getString(EMAIL_KEY, null)
    }

    fun saveName(name: String){
        sharedPreferences.edit().putString(NAME_KEY, name).apply()
    }

    fun getName(): String? {
        return sharedPreferences.getString(NAME_KEY, null)
    }

    fun savePhone(phone: String){
        sharedPreferences.edit().putString(PHONE_KEY, phone).apply()
    }

    fun getPhone(): String? {
        return sharedPreferences.getString(PHONE_KEY, null)
    }

    fun savePhoto(photo: String){
        sharedPreferences.edit().putString(PHOTO_KEY, photo).apply()
    }

    fun getPhoto(): String? {
        return sharedPreferences.getString(PHOTO_KEY, null)
    }

    fun saveNik(nik: String){
        sharedPreferences.edit().putString(NIK_KEY, nik).apply()
    }

    fun getNik(): String? {
        return sharedPreferences.getString(NIK_KEY, null)
    }

    fun saveBirthdate(birthdate: String){
        sharedPreferences.edit().putString(BIRTHDATE_KEY, birthdate).apply()
    }

    fun getBirthdate(): String? {
        return sharedPreferences.getString(BIRTHDATE_KEY, null)
    }

}