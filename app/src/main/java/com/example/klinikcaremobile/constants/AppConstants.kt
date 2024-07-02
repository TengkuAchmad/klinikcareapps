package com.example.klinikcaremobile.constants

class AppConstants {
    companion object {
        const val BASE_URL = "https://main--server-santa.netlify.app/.netlify/functions/api"
        const val USER_URL = "$BASE_URL/user-management/"
        const val OFFICER_URL = "$BASE_URL/officer-management/"
        const val TICKET_URL = "$BASE_URL/ticket-management/"
        const val MEDIC_URL = "$BASE_URL/datamedis-management/"
        const val PERSONEL_URL = "$BASE_URL/personel-management/"
    }
}