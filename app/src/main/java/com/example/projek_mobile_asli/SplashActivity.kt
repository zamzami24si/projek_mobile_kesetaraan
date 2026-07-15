package com.example.projek_mobile_asli

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Memanggil ID logo dari XML
        val btnLogo = findViewById<ImageButton>(R.id.imageButton)

        // Memberikan aksi saat logo diklik
        btnLogo.setOnClickListener {
            // Pindah ke Onboarding1Activity
            startActivity(Intent(this, Onboarding1Activity::class.java))
            finish()
        }
    }
}