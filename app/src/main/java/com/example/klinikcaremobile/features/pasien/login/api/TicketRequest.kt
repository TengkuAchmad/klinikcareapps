package com.example.klinikcaremobile.features.pasien.login.api

import android.util.Log
import com.example.klinikcaremobile.constants.AppConstants
import com.example.klinikcaremobile.features.pasien.login.storage.LoginStorage
import com.example.klinikcaremobile.features.pasien.login.storage.TicketStorage
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url


data class TicketDataResponse(
    @SerializedName("ticketNumber") val ticketNumber: String?,
    @SerializedName("totalTickets") val ticketTotal: Int?,
    @SerializedName("remainingTickets") val ticketRemaining: Int?,
    @SerializedName("remainingQueueMessage") val ticketQueue: String?,
)
interface ApiServiceGetDataTicket {
    @GET
    fun getDataTicket(@Url url: String, @retrofit2.http.Header("Authorization") token: String): Call<TicketDataResponse>
}

object ApiClientDataTicket {

    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(AppConstants.TICKET_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val apiServiceGetDataTicket: ApiServiceGetDataTicket = retrofit.create(ApiServiceGetDataTicket::class.java)
}
class TicketRequest( private val loginStorage: LoginStorage, private val ticketStorage: TicketStorage){

    fun getTicketData(callback: (Boolean, String) -> Unit) {
        val accessToken = loginStorage.getAccessToken()
        if (accessToken.isNullOrEmpty()){
            callback(false, "Access token is missing")
            return
        }

        val call = ApiClientDataTicket.apiServiceGetDataTicket.getDataTicket(url = AppConstants.TICKET_URL,"Bearer $accessToken")

        call.enqueue(object: retrofit2.Callback<TicketDataResponse> {
            override fun onResponse(call: Call<TicketDataResponse>, response: retrofit2.Response<TicketDataResponse>) {
                if (response.isSuccessful) {
                    val ticketData = response.body()
                    if (ticketData != null) {
                        saveTicketDataToIdentityStorage(ticketData)
                        callback(true, "Ticket data retrieved successfully")
                    } else {
                        Log.e("IdentityRequest", "Failed to retrieve ticket data: No data")
                        callback(false, "Failed to retrieve ticket data")
                    }
                } else {
                    Log.e("IdentityRequest", "Failed to retrieve user data: ${response.errorBody()?.string()}")
                    callback(false, "Failed to retrieve ticket data")
                }
            }

            override fun onFailure(call: Call<TicketDataResponse>, t: Throwable) {
                Log.e("IdentityRequest", "Network error: ${t.message}")
                callback(false, t.message ?: "Network error")
            }
        })
    }

    private fun saveTicketDataToIdentityStorage(ticketData: TicketDataResponse) {
        ticketStorage.saveTicketNumber(ticketData.ticketNumber?: "")
        ticketStorage.saveTotalTicket(ticketData.ticketTotal?: 0)
        ticketStorage.saveRemainingTicket(ticketData.ticketRemaining?: 0)
        ticketStorage.saveRemainingQueueTicket(ticketData.ticketQueue?: "")
    }
}