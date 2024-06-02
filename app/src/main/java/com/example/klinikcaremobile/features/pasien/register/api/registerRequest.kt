package com.example.klinikcaremobile.api

import android.util.Log
import com.example.klinikcaremobile.constants.AppConstants
import com.example.klinikcaremobile.features.pasien.login.storage.LoginStorage
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

data class RegisterUserRequestBody(
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    var birthdate: String,
    val nik: String,

)

data class RegisterUserResponse(
    val message: String
)

interface ApiServiceRegisterUser {
    @Headers("Content-Type: application/json")
    @POST("")
    fun register(@Url url: String = AppConstants.USER_URL, @Body requestBody: RegisterUserRequestBody): Call<RegisterUserResponse>
}
object ApiClientRegisterUser {
    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(AppConstants.USER_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val apiServiceRegisterUser: ApiServiceRegisterUser = retrofit.create(ApiServiceRegisterUser::class.java)
}

class RegisterRequest{
    fun performRegisterRequest(name: String, email: String, password: String, phone: String, birthdate: String, nik: String, callback: (Boolean, String) -> Unit) {
        val requestBody = RegisterUserRequestBody(name, email, password, phone, birthdate, nik)
        val jsonBody = Gson().toJson(requestBody)
        Log.d("LoginRequest", "Request body: $jsonBody")

        val call = ApiClientRegisterUser.apiServiceRegisterUser.register(url = AppConstants.USER_URL, requestBody)

        call.enqueue(object : retrofit2.Callback<RegisterUserResponse> {
            override fun onResponse(call: Call<RegisterUserResponse>, response: retrofit2.Response<RegisterUserResponse>) {
                if (response.isSuccessful) {
                    val registerUserResponse = response.body()
                    if (registerUserResponse != null ) {
                        callback(true, registerUserResponse.message)
                    } else {
                        val errorMsg = "Login failed. Please check your credentials."
                        Log.e("LoginRequest", errorMsg)
                        callback(false, "Login failed. Please check your credentials.")
                    }
                } else {
                    val errorMsg = "Error: ${response.errorBody()?.string()}"
                    Log.e("LoginRequest", errorMsg)
                    callback(false, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                callback(false, t.message ?: "Network error")
            }
        })
    }
}
