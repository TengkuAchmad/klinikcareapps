package com.example.klinikcaremobile.features.petugas.login.api

import android.util.Log
import com.example.klinikcaremobile.constants.AppConstants
import com.example.klinikcaremobile.features.petugas.login.storage.LoginStorage
import com.example.klinikcaremobile.features.petugas.login.storage.PersonelStorage
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

data class PersonelDataResponse(
    @SerializedName("UUID_MP") val uuid: String?,
    @SerializedName("Name_MP") val name: String?,
    @SerializedName("Role_MP") val role: String?
)

interface  ApiServiceGetDataPersonel {
    @GET
    fun getPersonelData(@Url url: String, @retrofit2.http.Header("Authorization") token: String): Call<List<PersonelDataResponse>>
}

object ApiClientDataPersonel {
    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(AppConstants.PERSONEL_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val apiServiceGetDataPersonel: ApiServiceGetDataPersonel = retrofit.create(ApiServiceGetDataPersonel::class.java)
}

class PersonelRequest( private val loginStorage: LoginStorage, private val personelStorage: PersonelStorage) {

    fun getPersonelData(callback: (Boolean, String) -> Unit) {
        val accessToken = loginStorage.getAccessToken()

        if (accessToken.isNullOrEmpty()) {
            callback(false, "Access token is missing")
            return
        }

        val call = ApiClientDataPersonel.apiServiceGetDataPersonel.getPersonelData(url = AppConstants.PERSONEL_URL, "Bearer $accessToken")

        call.enqueue(object: retrofit2.Callback<List<PersonelDataResponse>> {
            override fun onResponse(call: Call<List<PersonelDataResponse>>, response: retrofit2.Response<List<PersonelDataResponse>>) {
                if (response.isSuccessful) {
                    val personelData = response.body()

                    if (personelData != null && personelData.isNotEmpty())  {
                        savePersonelData(personelData)
                        callback(true, "Personel data retrieved successfully!")
                    } else {
                        callback(false, "Failed to retrieve personel data")
                    }
                } else {
                    callback(false, "Failed to retrieve personel data")
                }
            }

            override fun onFailure(call: Call<List<PersonelDataResponse>>, t: Throwable) {
                callback(false, t.message ?: "Network error")
            }
        })
    }

    private fun savePersonelData(personelDataResponse: List<PersonelDataResponse>){
        val personelUuid = personelDataResponse.mapNotNull { it.uuid }
        val personelName = personelDataResponse.mapNotNull { it.name }
        val personelRole = personelDataResponse.mapNotNull { it.role }

        personelStorage.saveUuidPersonel(personelUuid)
        personelStorage.saveNamePersonel(personelName)
        personelStorage.saveRolePersonel(personelRole)

    }
}