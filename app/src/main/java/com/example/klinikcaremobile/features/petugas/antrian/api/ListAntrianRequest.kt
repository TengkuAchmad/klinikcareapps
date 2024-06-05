package com.example.klinikcaremobile.features.petugas.antrian.api

import android.util.Log
import com.example.klinikcaremobile.constants.AppConstants
import com.example.klinikcaremobile.features.petugas.login.storage.LoginStorage
import com.example.klinikcaremobile.features.petugas.antrian.storage.ListAntrianStorage
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

data class UserAccount(
    @SerializedName("UUID_UA") val uuid: String?,
    @SerializedName("Name_UA") val name: String?,
    @SerializedName("NIK_UA") val nik: String?
)

data class Ticket(
    @SerializedName("Nomor_TC") val nomorTicket: String?,
    @SerializedName("UserAccount") val userAccount: UserAccount?
)
interface ApiServiceGetDataTicketList {
    @GET("list/waiting")
    fun getDataTicketList(@Header("Authorization") token: String): Call<List<Ticket>>
}

object ApiClientDataTicketList {
    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(AppConstants.TICKET_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val apiServiceGetDataTicketList: ApiServiceGetDataTicketList = retrofit.create(ApiServiceGetDataTicketList::class.java)
}


class TicketListRequest(private val loginStorage: LoginStorage, private val listAntrianStorage: ListAntrianStorage) {
    fun getTicketListData(callback: (Boolean, String) -> Unit){
        val accessToken = loginStorage.getAccessToken()
        if(accessToken.isNullOrEmpty()){
            callback(false, "Access Token is missing")
            return
        }

        val call = ApiClientDataTicketList.apiServiceGetDataTicketList.getDataTicketList("Bearer $accessToken")

        call.enqueue(object : retrofit2.Callback<List<Ticket>> {
            override fun onResponse(call: Call<List<Ticket>>, response: retrofit2.Response<List<Ticket>>) {
                if (response.isSuccessful) {
                    val tickets = response.body()
                    Log.d("Waiting List Tickets :", tickets.toString())
                    if (tickets != null && tickets.isNotEmpty()) {
                       saveTicketListData(tickets)
                        callback(true, "Ticket List Data retrieved successfully")
                    } else {
                        Log.e("TicketOfficerRequest", "Failed to retrieve ticket list data: No tickets")
                        callback(false, "Failed to retrieve ticket data")
                    }

                } else {
                    Log.e("TicketOfficerRequest", "Failed to retrieve ticket list data: No tickets")
                    callback(false, "Failed to retrieve ticket data")
                }
            }

            override fun onFailure(call: Call<List<Ticket>>, t: Throwable) {
                Log.e("TicketListRequest", "Network error: ${t.message}")
                callback(false, t.message ?: "Network error")
            }
        })
    }

    private fun saveTicketListData(tickets: List<Ticket>){
        val ticketNumbers = tickets.mapNotNull { it.nomorTicket }
        val userNames = tickets.map { it.userAccount?.name }
        val userNiks = tickets.map { it.userAccount?.nik }

        Log.d("TicketList", "Ticket Numbers: $ticketNumbers")
        Log.d("TicketList", "User Names: $userNames")
        Log.d("TicketList", "User Niks: $userNiks")

        listAntrianStorage.saveTicketNumbers(ticketNumbers)
        listAntrianStorage.saveTicketNames(userNames)
        listAntrianStorage.saveTicketNiks(userNiks)
    }

}