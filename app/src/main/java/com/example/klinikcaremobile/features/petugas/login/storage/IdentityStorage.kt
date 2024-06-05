package com.example.klinikcaremobile.features.petugas.login.storage

import android.content.Context
import android.content.SharedPreferences

class IdentityOfficerStorage(context: Context){
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "storage",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val EMAIL_KEY = "emailOfficer"
        private const val NAME_KEY = "nameOfficer"
        private const val PHONE_KEY = "phoneOfficer"
        private const val PHOTO_KEY = "photoOfficer"
        private const val NIP_KEY = "nipOfficer"
        private const val BIRTHDATE_KEY = "birthdateOfficer"
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

    fun saveNip(nip: String){
        sharedPreferences.edit().putString(NIP_KEY, nip).apply()
    }

    fun getNik(): String? {
        return sharedPreferences.getString(NIP_KEY, null)
    }

    fun saveBirthdate(birthdate: String){
        sharedPreferences.edit().putString(BIRTHDATE_KEY, birthdate).apply()
    }

    fun getBirthdate(): String? {
        return sharedPreferences.getString(BIRTHDATE_KEY, null)
    }

}