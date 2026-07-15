package com.example.projek_mobile_asli

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Onboarding2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding2)

        val btnStart2 = findViewById<Button>(R.id.btnstart2)
        val btnSkip2 = findViewById<Button>(R.id.btnskip2)

        btnStart2.setOnClickListener {
            // Lanjut ke halaman Onboarding 3
            startActivity(Intent(this, Onboarding3Activity::class.java))
        }

        btnSkip2.setOnClickListener {
            // Nanti diarahkan ke halaman Login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}