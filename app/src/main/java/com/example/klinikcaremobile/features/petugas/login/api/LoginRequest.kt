package com.example.klinikcaremobile.features.petugas.login.api

import android.util.Log
import com.example.klinikcaremobile.constants.AppConstants
import com.example.klinikcaremobile.features.petugas.login.storage.LoginStorage
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class LoginRequestBody(
    val email: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val accessToken: String
)

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("auth")
    fun login(@Body requestBody: LoginRequestBody): Call<LoginResponse>
}

object ApiClient {
    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(AppConstants.OFFICER_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

class LoginRequest(private val loginStorage: LoginStorage) {

    fun performLoginRequest(email: String, password: String, callback: (Boolean, String) -> Unit) {
        val requestBody = LoginRequestBody(email, password)
        val jsonBody = Gson().toJson(requestBody)
        Log.d("LoginRequest", "Request body: $jsonBody")
        val call = ApiClient.apiService.login(requestBody)

        call.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: retrofit2.Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.success) {
                        val accessToken = loginResponse.accessToken
                        loginStorage.saveAccessToken(accessToken)

                        callback(true, loginResponse.accessToken)
                    } else {
                        val errorMsg = "Oops, Username dan password tidak cocok!"
                        Log.e("LoginRequest", errorMsg)
                        callback(false, "Oops, Username dan password tidak cocok!")
                    }
                } else {
                    callback(false, "Oops, Username dan password tidak cocok!")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(false, t.message ?: "Network error")
            }
        })
    }
}
