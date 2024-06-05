package com.example.klinikcaremobile.features.petugas.riwayat.api

import android.util.Log
import com.example.klinikcaremobile.constants.AppConstants
import com.example.klinikcaremobile.features.petugas.login.storage.LoginStorage
import com.example.klinikcaremobile.features.petugas.riwayat.storage.RiwayatStorage
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

data class RiwayatResponse(
    @SerializedName("NIK_UA") val nik: String?,
    @SerializedName("Name_UA") val name: String?
)

interface ApiServiceGetDataRiwayatList {
    @GET("checkup")
    fun getDataRiwayatList(@Header("Authorization") token: String): Call<List<RiwayatResponse>>
}

object ApiClientDataRiwayatList {
    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(AppConstants.USER_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val apiServiceGetDataRiwayatList: ApiServiceGetDataRiwayatList = retrofit.create(ApiServiceGetDataRiwayatList::class.java)
}

class RiwayatRequest(private val loginStorage: LoginStorage, private val riwayatStorage: RiwayatStorage) {
    fun getRiwayatListData(callback: (Boolean, String) -> Unit){
        val accessToken = loginStorage.getAccessToken()
        if(accessToken.isNullOrEmpty()){
            callback(false, "Access Token is missing")
            return
        }

        val call = ApiClientDataRiwayatList.apiServiceGetDataRiwayatList.getDataRiwayatList("Bearer $accessToken")

        call.enqueue(object: retrofit2.Callback<List<RiwayatResponse>> {
            override fun onResponse(
                call: Call<List<RiwayatResponse>>,
                response: Response<List<RiwayatResponse>>
            ) {
                if (response.isSuccessful) {
                    val riwayats = response.body()
                        Log.d("Data Riwayat", riwayats.toString())

                    if (riwayats != null && riwayats.isNotEmpty()) {
                        val riwayatsWithNumbers = riwayats.mapIndexed { index, riwayat ->
                            RiwayatResponse((index + 1).toString(), riwayat.name)
                        }
                        saveRiwayatListData(riwayatsWithNumbers)

                        callback(true, "Riwayat List Data retrieved successfully")
                    } else {
                        callback(false, "Failed to retrieve ticket data")
                    }
                } else {
                    callback(false, "Failed to retrieve ticket data")
                }
            }

            override fun onFailure(call: Call<List<RiwayatResponse>>, t: Throwable) {
                callback(false, t.message ?: "Network error")
            }
        })
    }

    private fun saveRiwayatListData(riwayats: List<RiwayatResponse>){
        val idNumbers = riwayats.mapNotNull { it.nik }
        val userNames = riwayats.mapNotNull { it.name }

        riwayatStorage.saveRiwayatID(idNumbers)
        riwayatStorage.saveRiwayatName(userNames)
    }
}