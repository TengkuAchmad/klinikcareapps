package com.example.klinikcaremobile.features.pasien.appointment.api

import android.util.Log
import com.example.klinikcaremobile.constants.AppConstants
import com.example.klinikcaremobile.features.pasien.appointment.storage.TicketInfoStorage
import com.example.klinikcaremobile.features.pasien.login.storage.IdentityStorage
import com.example.klinikcaremobile.features.pasien.login.storage.LoginStorage
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url


data class TicketInfoResponse(
    @SerializedName("Date_TC") val date: String?,
    @SerializedName("Time_TC") val time: String?,
    @SerializedName("Nomor_TC") val number: String?,
)
interface ApiServiceGetDataTicketInfo {
    @GET
    fun getTicketInfoData(@Url url: String, @retrofit2.http.Header("Authorization") token: String): Call<TicketInfoResponse>
}

object ApiClientTicketInfo {


    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(AppConstants.TICKET_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val apiServiceGetTicketInfo: ApiServiceGetDataTicketInfo = retrofit.create(ApiServiceGetDataTicketInfo::class.java)
}
class TicketInfoRequest( private val loginStorage: LoginStorage,private val identityStorage: IdentityStorage,  private val ticketInfoStorage: TicketInfoStorage){

    fun getTicketInfoData(callback: (Boolean, String) -> Unit) {
        val accessToken = loginStorage.getAccessToken()
        if (accessToken.isNullOrEmpty()){
            callback(false, "Access token is missing")
            return
        }

        val uuidUser = identityStorage.getUUID().toString()

        val endpoint = "${AppConstants.TICKET_URL}/${uuidUser}"

        val call = ApiClientTicketInfo.apiServiceGetTicketInfo.getTicketInfoData(url = endpoint,"Bearer $accessToken")

        call.enqueue(object: retrofit2.Callback<TicketInfoResponse> {
            override fun onResponse(call: Call<TicketInfoResponse>, response: retrofit2.Response<TicketInfoResponse>) {
                if (response.isSuccessful) {
                    val ticketInfoData = response.body()
                    Log.d("Response", ticketInfoData.toString())
                    if (ticketInfoData != null) {
                        saveTicketInfoToStorage(ticketInfoData)
                        callback(true, "Ticket info data retrieved successfully")
                    } else {
                        Log.e("TicketInfoRequest", "Failed to retrieve ticket info data: No data")
                        callback(false, "Failed to retrieve ticket info data")
                    }
                } else {
                    Log.e("TicketInfoRequest", "Failed to retrieve ticket info data: ${response.errorBody()?.string()}")
                    callback(false, "Failed to retrieve ticket info data")
                }
            }

            override fun onFailure(call: Call<TicketInfoResponse>, t: Throwable) {
                Log.e("IdentityRequest", "Network error: ${t.message}")
                callback(false, t.message ?: "Network error")
            }
        })
    }

    private fun saveTicketInfoToStorage(ticketInfoData: TicketInfoResponse) {
        ticketInfoStorage.saveTicketNumber(ticketInfoData.number.toString())
        ticketInfoStorage.saveDatetimeTicket(ticketInfoData.date.toString(), ticketInfoData.time.toString())
    }
}