package com.example.klinikcaremobile.features.petugas.login.api

import android.util.Log
import com.example.klinikcaremobile.constants.AppConstants
import com.example.klinikcaremobile.features.petugas.login.storage.LoginStorage
import com.example.klinikcaremobile.features.petugas.login.storage.IdentityOfficerStorage
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url


data class OfficerDataResponse(
    @SerializedName("Name_UA") val name: String?,
    @SerializedName("Email_UA") val email: String?,
    @SerializedName("Phone_UA") val phone: String?,
    @SerializedName("Photo_UA") val photo: String?,
    @SerializedName("NIP_UA") val nip: String?,
    @SerializedName("BirthDate_UA") val birthdate: String?
)
interface ApiServiceGetDataOfficer {
    @GET
    fun getOfficerData(@Url url: String, @retrofit2.http.Header("Authorization") token: String): Call<OfficerDataResponse>
}

object ApiClientDataOfficer {

    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(AppConstants.OFFICER_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val apiServiceGetDataOfficer: ApiServiceGetDataOfficer = retrofit.create(ApiServiceGetDataOfficer::class.java)
}
class IdentityRequest( private val loginStorage: LoginStorage, private val identityOfficerStorage: IdentityOfficerStorage){

    fun getOfficerData(callback: (Boolean, String) -> Unit) {
        val accessToken = loginStorage.getAccessToken()
        if (accessToken.isNullOrEmpty()){
            callback(false, "Access token is missing")
            return
        }

        Log.d("IdentityRequest", "Access token: Bearer $accessToken")

        val call = ApiClientDataOfficer.apiServiceGetDataOfficer.getOfficerData(url = AppConstants.OFFICER_URL,"Bearer $accessToken")

        call.enqueue(object: retrofit2.Callback<OfficerDataResponse> {
            override fun onResponse(call: Call<OfficerDataResponse>, response: retrofit2.Response<OfficerDataResponse>) {
                if (response.isSuccessful) {
                    val officerData = response.body()
                    if (officerData != null) {
                        saveOfficerDataToIdentityStorage(officerData)
                        callback(true, "Officer data retrieved successfully")
                    } else {
                        callback(false, "Failed to retrieve officer data")
                    }
                } else {
                    Log.e("IdentityRequest", "Failed to retrieve officer data: ${response.errorBody()?.string()}")
                    callback(false, "Failed to retrieve officer data")
                }
            }

            override fun onFailure(call: Call<OfficerDataResponse>, t: Throwable) {
                Log.e("IdentityRequest", "Network error: ${t.message}")
                callback(false, t.message ?: "Network error")
            }
        })
    }

    private fun saveOfficerDataToIdentityStorage(userData: OfficerDataResponse) {
        identityOfficerStorage.saveEmail(userData.email ?: "")
        identityOfficerStorage.saveName(userData.name ?: "")
        identityOfficerStorage.savePhone(userData.phone ?: "")
        identityOfficerStorage.savePhoto(userData.photo ?: "")
        identityOfficerStorage.saveNip(userData.nip ?: "")
        identityOfficerStorage.saveBirthdate(userData.birthdate ?: "")
    }
}