import android.util.Log
import com.example.klinikcaremobile.constants.AppConstants
import com.example.klinikcaremobile.features.petugas.login.storage.LoginStorage
import com.example.klinikcaremobile.features.petugas.login.storage.TicketOfficerStorage
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header


data class WaitingTicketResponse(
    @SerializedName("waitingTicket") val waitingTicket: Int?,
    @SerializedName("waitingCount") val waitingCount: Int?,
    @SerializedName("completedCount") val completedCount: Int?
)

interface ApiServiceGetDataTicketOfficer {
    @GET("find/waiting")
    fun getDataTicketOfficer(@Header("Authorization") token: String): Call<WaitingTicketResponse>
}

object ApiClientDataTicketOfficer {

    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(AppConstants.TICKET_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val apiServiceGetDataTicketOfficer: ApiServiceGetDataTicketOfficer = retrofit.create(ApiServiceGetDataTicketOfficer::class.java)
}

class TicketOfficerRequest(private val loginStorage: LoginStorage, private val ticketStorage: TicketOfficerStorage){

    fun getTicketOfficerData(callback: (Boolean, String) -> Unit) {
        val accessToken = loginStorage.getAccessToken()
        if (accessToken.isNullOrEmpty()){
            callback(false, "Access token is missing")
            return
        }

        val call = ApiClientDataTicketOfficer.apiServiceGetDataTicketOfficer.getDataTicketOfficer("Bearer $accessToken")

        call.enqueue(object: retrofit2.Callback<WaitingTicketResponse> {
            override fun onResponse(call: Call<WaitingTicketResponse>, response: retrofit2.Response<WaitingTicketResponse>) {
                if (response.isSuccessful) {
                    val waitingTicketResponse = response.body()
                    if (waitingTicketResponse != null) {
                        val waitingTicket = waitingTicketResponse.waitingTicket
                        val waitingCount = waitingTicketResponse.waitingCount
                        val completedCount = waitingTicketResponse.completedCount

                        if (waitingTicket != null) {
                            saveTicketOfficerData(waitingTicket ?: 0, waitingCount ?: 0, completedCount ?:0 )
                            callback(true, "Ticket data retrieved successfully")
                        } else {
                            Log.e("IdentityRequest", "Failed to retrieve ticket data: No waiting ticket")
                            callback(false, "Failed to retrieve ticket data")
                        }
                    } else {
                        Log.e("IdentityRequest", "Failed to retrieve ticket data: No data")
                        callback(false, "Failed to retrieve ticket data")
                    }
                } else {
                    Log.e("IdentityRequest", "Failed to retrieve user data: ${response.errorBody()?.string()}")
                    callback(false, "Failed to retrieve ticket data")
                }
            }

            override fun onFailure(call: Call<WaitingTicketResponse>, t: Throwable) {
                Log.e("IdentityRequest", "Network error: ${t.message}")
                callback(false, t.message ?: "Network error")
            }
        })
    }

    private fun saveTicketOfficerData(ticketNumber: Int, waitingCount: Int, completedCount: Int) {
        ticketStorage.saveTotalWaitingTicket(waitingCount)
        ticketStorage.saveWaitingTicketNumber(ticketNumber)
        ticketStorage.saveCompletedTicketNumber(completedCount)
    }
}
