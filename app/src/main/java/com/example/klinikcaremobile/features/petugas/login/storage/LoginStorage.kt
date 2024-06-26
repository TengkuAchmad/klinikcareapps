package com.example.klinikcaremobile.features.petugas.login.storage

import android.content.Context
import android.content.SharedPreferences

class LoginStorage(context: Context){
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "storage",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val ACCESS_TOKEN_KEY = "accessTokenOfficer"
    }

    fun saveAccessToken(accessToken: String) {
        sharedPreferences.edit().putString(ACCESS_TOKEN_KEY, accessToken).apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }
}