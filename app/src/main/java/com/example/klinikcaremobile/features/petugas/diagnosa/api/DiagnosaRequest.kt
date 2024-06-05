package com.example.klinikcaremobile.features.petugas.diagnosa.api

import android.util.Log
import com.example.klinikcaremobile.constants.AppConstants
import com.example.klinikcaremobile.features.petugas.login.storage.LoginStorage
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

data class DiagnosaRequestBody(
    val nik: String,
    val diagnosis: String,
    val obat: String,
    val alergi: String
)

data class DiagnosaResponse(
    val message: String,
)

interface  ApiServiceCreateDiagnosa {
    @Headers("Content-Type: application/json")
    @POST("")
    fun create(@Url url: String= AppConstants.MEDIC_URL, @Body requestBody: DiagnosaRequestBody, @retrofit2.http.Header("Authorization") token: String) : Call<DiagnosaResponse>
}

object ApiClientCreateDiagnosa {
    private val retrofit  = retrofit2.Retrofit.Builder()
        .baseUrl(AppConstants.MEDIC_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val apiServiceCreateDiagnosa : ApiServiceCreateDiagnosa = retrofit.create(ApiServiceCreateDiagnosa::class.java)
}
class DiagnosaRequest ( private val loginStorage: LoginStorage){
    fun performCreateDiagnosa(nik: String, diagnosis: String, obat: String, alergi: String, callback: (Boolean, String) -> Unit) {
        val accessToken = loginStorage.getAccessToken()
        if (accessToken.isNullOrEmpty()){
            callback(false, "Access token is missing")
            return
        }

        val requestBody = DiagnosaRequestBody(nik, diagnosis, obat, alergi)

        val call = ApiClientCreateDiagnosa.apiServiceCreateDiagnosa.create(url = AppConstants.MEDIC_URL, requestBody,"Bearer $accessToken" )

        call.enqueue(object: retrofit2.Callback<DiagnosaResponse> {
            override fun onResponse(call: Call<DiagnosaResponse>, response: retrofit2.Response<DiagnosaResponse>) {
                if (response.isSuccessful) {
                    val createDiagnosaResponse = response.body()
                    if (createDiagnosaResponse != null) {
                        callback(true, createDiagnosaResponse.message)
                    } else {
                        Log.e("LoginRequest", "${response.errorBody()?.string()}")
                        callback(false, "Create the diagnose is failed")
                    }
                } else {
                    val errorMsg =  response.errorBody().toString()
                    Log.e("LoginRequest", "${response.errorBody()?.string()}")
                    callback(false, "Create the diagnose is failed")
                }
            }

            override fun onFailure(call: Call<DiagnosaResponse>, t: Throwable) {
                callback(false, t.message ?: "Network error")
            }
        })
    }
}