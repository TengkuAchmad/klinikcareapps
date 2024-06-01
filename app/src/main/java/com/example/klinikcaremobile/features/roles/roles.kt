package com.example.klinikcaremobile.features.roles

import android.content.Intent
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.klinikcaremobile.R
import com.example.klinikcaremobile.features.pasien.register.activity.register_user
import com.example.klinikcaremobile.features.petugas.register.activity.register_petugas

class Roles : AppCompatActivity() {

    private lateinit var officerImageView: ImageView
    private lateinit var userImageView: ImageView
    private lateinit var buttonSplashScreenView: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            enableEdgeToEdge()
            setContentView(R.layout.activity_roles)

            officerImageView = findViewById(R.id.officer_images)
            userImageView = findViewById(R.id.user_images)
            buttonSplashScreenView = findViewById(R.id.button_splashscreen)


            applyGrayscaleFilter(officerImageView)

            officerImageView.setOnClickListener {
                toggleImageColor(officerImageView, userImageView, "Officer image clicked")
            }

            userImageView.setOnClickListener {
                toggleImageColor(userImageView, officerImageView, "User image clicked")
            }

            buttonSplashScreenView.setOnClickListener {
                navigateToNextPage()
            }

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun toggleImageColor(clickedImageView: ImageView, otherImageView: ImageView, message: String) {
        clickedImageView.clearColorFilter()

        applyGrayscaleFilter(otherImageView)

    }

    private fun applyGrayscaleFilter(imageView: ImageView) {
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val colorMatrixFilter = ColorMatrixColorFilter(colorMatrix)
        imageView.colorFilter = colorMatrixFilter
    }

    private fun navigateToNextPage() {
        val isOfficerActive = officerImageView.colorFilter == null
        val isUserActive = userImageView.colorFilter == null
        if (isOfficerActive) {
            val intent = Intent(this, register_petugas::class.java)
            startActivity(intent)
        } else if (isUserActive) {
            val intent  = Intent(this, register_user::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "No active role selected", Toast.LENGTH_SHORT).show()
        }
    }
}
