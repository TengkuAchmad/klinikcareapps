package com.example.klinikcaremobile.features.pasien.records.api

import android.util.Log
import com.example.klinikcaremobile.constants.AppConstants
import com.example.klinikcaremobile.features.pasien.login.storage.LoginStorage
import com.example.klinikcaremobile.features.pasien.records.storage.RecordStorage
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

data class MedicPersonel(
    @SerializedName("Name_MP") val name: String?
)
data class RecordResponse(
    @SerializedName("Diagnosis_DM") val diagnosis: String?,
    @SerializedName("Obat_DM") val obat: String?,
    @SerializedName("Alergi_DM") val alergi: String?,
    @SerializedName("createdAt") val time: String?,
    @SerializedName("MedicPersonel") val medicPersonel: MedicPersonel?
)

interface ApiServiceGetDataRecordList {
    @GET("")
    fun getDataRecordList(@Url url : String, @Header("Authorization") token: String) : Call<List<RecordResponse>>
}

object ApiClientGetDataRecordList {
    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(AppConstants.MEDIC_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val apiServiceGetDataRecordList: ApiServiceGetDataRecordList =
        retrofit.create(ApiServiceGetDataRecordList::class.java)
}

class RecordRequest (private val loginStorage: LoginStorage, private val recordStorage: RecordStorage){
    fun getRecordList(callback: (Boolean, String) -> Unit) {
        val accessToken = loginStorage.getAccessToken()
        if (accessToken.isNullOrEmpty()){
            callback(false, "Access Token is missing")
            return
        }

        val call = ApiClientGetDataRecordList.apiServiceGetDataRecordList.getDataRecordList(AppConstants.MEDIC_URL, "Bearer $accessToken")

        call.enqueue(object: retrofit2.Callback<List<RecordResponse>> {
            override fun onResponse(
                call: Call<List<RecordResponse>>,
                response: Response<List<RecordResponse>>
            ) {
                if (response.isSuccessful){
                    val records = response.body()

                    if (!records.isNullOrEmpty()) {
                        Log.d("Data", records.toString())
                        saveRecordListData(records)

                        callback(true, "Rekam Medis Data retrived successfully!")
                    } else {
                        callback(false,"Failed to retrive rekam medis data")
                    }
                } else {
                    callback(false, "Failed to retrive rekam medis data")
                }
            }

            override fun onFailure(call: Call<List<RecordResponse>>, t: Throwable) {
                callback(false, t.message?: "Network Error")
            }
        })
    }

    private  fun saveRecordListData(records: List<RecordResponse>){
        val diagnose = records.mapNotNull {  it.diagnosis }
        val obat = records.mapNotNull { it.obat }
        val alergi = records.mapNotNull { it.alergi }
        val time = records.mapNotNull { it.time }
        val name = records.map { it.medicPersonel?.name }

        Log.d("NAMEPERSONEL", name.toString())

        recordStorage.saveRecordDiagnose(diagnose)
        recordStorage.saveRecordObat(obat)
        recordStorage.saveRecordAlergi(alergi)
        recordStorage.saveRecordTime(time)
        recordStorage.saveRecordPersonel(name)


    }
}