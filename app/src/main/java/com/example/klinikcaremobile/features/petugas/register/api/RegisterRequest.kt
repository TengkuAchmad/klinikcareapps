package com.example.klinikcaremobile.api

import android.util.Log
import com.example.klinikcaremobile.constants.AppConstants
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

data class RegisterOfficerRequestBody(
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    var birthdate: String,
    val nip: String,

    )

data class RegisterOfficerResponse(
    val message: String
)

interface ApiServiceRegisterOfficer {
    @Headers("Content-Type: application/json")
    @POST("")
    fun register(@Url url: String = AppConstants.OFFICER_URL, @Body requestBody: RegisterOfficerRequestBody): Call<RegisterOfficerResponse>
}
object ApiClientRegisterOfficer {
    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(AppConstants.OFFICER_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val apiServiceRegisterOfficer: ApiServiceRegisterOfficer = retrofit.create(ApiServiceRegisterOfficer::class.java)
}

class RegisterOfficerRequest{
    fun performRegisterRequest(name: String, email: String, password: String, phone: String, birthdate: String, nip: String, callback: (Boolean, String) -> Unit) {
        val requestBody = RegisterOfficerRequestBody(name, email, password, phone, birthdate, nip)
        val jsonBody = Gson().toJson(requestBody)
        Log.d("LoginRequest", "Request body: $jsonBody")

        val call = ApiClientRegisterOfficer.apiServiceRegisterOfficer.register(url = AppConstants.OFFICER_URL, requestBody)

        call.enqueue(object : retrofit2.Callback<RegisterOfficerResponse> {
            override fun onResponse(call: Call<RegisterOfficerResponse>, response: retrofit2.Response<RegisterOfficerResponse>) {
                if (response.isSuccessful) {
                    val registerOfficerResponse = response.body()
                    if (registerOfficerResponse != null ) {
                        callback(true, registerOfficerResponse.message)
                    } else {
                        val errorMsg = "Login failed. Please check your credentials."
                        Log.e("LoginRequest", errorMsg)
                        callback(false, "Login failed. Please check your credentials.")
                    }
                } else {
                    val errorMsg = "Error: ${response.errorBody()?.string()}"
                    Log.e("LoginRequest", errorMsg)
                    callback(false, "Error: $errorMsg")
                }
            }

            override fun onFailure(call: Call<RegisterOfficerResponse>, t: Throwable) {
                callback(false, t.message ?: "Network error")
            }
        })
    }
}
