package com.example.klinikcaremobile.features.pasien.login.api

import android.util.Log
import com.example.klinikcaremobile.constants.AppConstants
import com.example.klinikcaremobile.features.pasien.login.storage.IdentityStorage
import com.example.klinikcaremobile.features.pasien.login.storage.LoginStorage
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url


data class UserDataResponse(
    @SerializedName("UUID_UA") val uuidUser: String?,
    @SerializedName("Name_UA") val name: String?,
    @SerializedName("Email_UA") val email: String?,
    @SerializedName("Phone_UA") val phone: String?,
    @SerializedName("Photo_UA") val photo: String?,
    @SerializedName("NIK_UA") val nik: String?,
    @SerializedName("BirthDate_UA") val birthdate: String?
)
interface ApiServiceGetDataUser {
    @GET
    fun getUserData(@Url url: String, @retrofit2.http.Header("Authorization") token: String): Call<UserDataResponse>
}

object ApiClientDataUser {

    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(AppConstants.USER_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val apiServiceGetDataUser: ApiServiceGetDataUser = retrofit.create(ApiServiceGetDataUser::class.java)
}
class IdentityRequest( private val loginStorage: LoginStorage, private val identityStorage: IdentityStorage){

    fun getUserData(callback: (Boolean, String) -> Unit) {
        val accessToken = loginStorage.getAccessToken()
        if (accessToken.isNullOrEmpty()){
            callback(false, "Access token is missing")
            return
        }

        Log.d("IdentityRequest", "Access token: Bearer $accessToken")

        val call = ApiClientDataUser.apiServiceGetDataUser.getUserData(url = AppConstants.USER_URL,"Bearer $accessToken")

        call.enqueue(object: retrofit2.Callback<UserDataResponse> {
            override fun onResponse(call: Call<UserDataResponse>, response: retrofit2.Response<UserDataResponse>) {
                if (response.isSuccessful) {
                    val userData = response.body()
                    Log.d("Response", userData.toString())
                    if (userData != null) {
                        saveUserDataToIdentityStorage(userData)
                        Log.d("LoginRequest", userData.toString())
                        callback(true, "User data retrieved successfully")
                    } else {
                        Log.e("IdentityRequest", "Failed to retrieve user data: No data")
                        callback(false, "Failed to retrieve user data")
                    }
                } else {
                    Log.e("IdentityRequest", "Failed to retrieve user data: ${response.errorBody()?.string()}")
                    callback(false, "Failed to retrieve user data")
                }
            }

            override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                Log.e("IdentityRequest", "Network error: ${t.message}")
                callback(false, t.message ?: "Network error")
            }
        })
    }

    private fun saveUserDataToIdentityStorage(userData: UserDataResponse) {
        identityStorage.saveUUID(userData.uuidUser ?: "")
        identityStorage.saveEmail(userData.email ?: "")
        identityStorage.saveName(userData.name ?: "")
        identityStorage.savePhone(userData.phone ?: "")
        identityStorage.savePhoto(userData.photo ?: "")
        identityStorage.saveNik(userData.nik ?: "")
        identityStorage.saveBirthdate(userData.birthdate ?: "")
    }
}