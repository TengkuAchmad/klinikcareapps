package com.example.klinikcaremobile.features.pasien.appointment.api

import android.util.Log
import com.example.klinikcaremobile.constants.AppConstants
import com.example.klinikcaremobile.features.pasien.login.storage.LoginStorage
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url


data class AppointmentRequestBody (
    val date: String,
    val time: String
)

data class AppointmentResponse (
    val success: Boolean,
    val message : String
)

interface ApiAppointmentService {
    @Headers("Content-Type: application/json")
    @POST()
    fun appointment(@Url url: String, @Header("Authorization") token: String,@Body requestBody: AppointmentRequestBody): Call<AppointmentResponse>
}

object ApiAppointmentClient {
    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(AppConstants.TICKET_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val apiService: ApiAppointmentService = retrofit.create(ApiAppointmentService::class.java)
}
class AppointmentRequest (private val loginStorage: LoginStorage){

    fun performAppointmentRequest(date: String, time: String, callback: (Boolean, String) -> Unit) {
        val accessToken = loginStorage.getAccessToken()
        if (accessToken.isNullOrEmpty()) {
            callback(false, "Access token is missing")
            return
        }

        val requestBody = AppointmentRequestBody(date, time)
        val jsonBody = Gson().toJson(requestBody)

        val call = ApiAppointmentClient.apiService.appointment(AppConstants.TICKET_URL,"Bearer $accessToken", requestBody)

        call.enqueue(object: retrofit2.Callback<AppointmentResponse> {
            override fun onResponse(call: Call<AppointmentResponse>, response: retrofit2.Response<AppointmentResponse>) {
                if (response.isSuccessful){
                    val appointmentResponse = response.body()
                    if (appointmentResponse != null  && appointmentResponse.success) {
                        callback(true, appointmentResponse.message)
                    } else {
                        Log.e("IdentityRequest", "Failed to retrieve user data: ${response.errorBody()?.string()}")
                        callback(false, "Pengajuan gagal, silakan coba lagi!")
                    }
                } else {
                    Log.e("IdentityRequest", "Failed to retrieve user data: ${response.errorBody()?.string()}")
                    callback(false, "Pengajuan gagal, silakan coba lagi!")
                }
            }

            override fun onFailure(call: Call<AppointmentResponse>, t: Throwable) {
                Log.e("IdentityRequest", "Network error: ${t.message}")
                callback(false, t.message ?: "Network error")
            }
        })
    }
}