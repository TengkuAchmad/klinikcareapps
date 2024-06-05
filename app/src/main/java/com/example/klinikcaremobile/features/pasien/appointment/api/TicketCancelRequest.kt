package com.example.klinikcaremobile.features.pasien.appointment.api

import android.util.Log
import com.example.klinikcaremobile.constants.AppConstants
import com.example.klinikcaremobile.features.pasien.login.storage.IdentityStorage
import com.example.klinikcaremobile.features.pasien.login.storage.LoginStorage
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url


data class AppointmentCancelResponse (
    val message : String
)
interface ApiAppointmentCancelService {
    @Headers("Content-Type: application/json")
    @POST()
    fun appointment(@Url url: String, @Header("Authorization") token: String): Call<AppointmentCancelResponse>
}

object ApiAppointmentCancelClient {
    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(AppConstants.TICKET_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val apiService: ApiAppointmentCancelService = retrofit.create(ApiAppointmentCancelService::class.java)
}
class AppointmentCancelRequest (private val loginStorage: LoginStorage, private val identityStorage: IdentityStorage){

    fun performAppointmentCancelRequest(callback: (Boolean, String) -> Unit) {
        val accessToken = loginStorage.getAccessToken()
        if (accessToken.isNullOrEmpty()) {
            callback(false, "Access token is missing")
            return
        }

        val uuidUser = identityStorage.getUUID().toString()

        val endpoint = "${AppConstants.TICKET_URL}/cancel/${uuidUser}"

        val call = ApiAppointmentCancelClient.apiService.appointment(endpoint,"Bearer $accessToken")

        call.enqueue(object: retrofit2.Callback<AppointmentCancelResponse> {
            override fun onResponse(call: Call<AppointmentCancelResponse>, response: retrofit2.Response<AppointmentCancelResponse>) {
                if (response.isSuccessful){
                    val appointmentResponse = response.body()
                    if (appointmentResponse != null) {
                        callback(true, appointmentResponse.message)
                    } else {
                        Log.e("IdentityRequest", "Failed to retrieve user data: ${response.errorBody()?.string()}")
                        callback(false, "Pengajuan pembatalan gagal, silakan coba lagi!")
                    }
                } else {
                    Log.e("IdentityRequest", "Failed to retrieve user data: ${response.errorBody()?.string()}")
                    callback(false, "Pengajuan pembatalan gagal, silakan coba lagi!")
                }
            }

            override fun onFailure(call: Call<AppointmentCancelResponse>, t: Throwable) {
                Log.e("IdentityRequest", "Network error: ${t.message}")
                callback(false, t.message ?: "Network error")
            }
        })
    }
}