package com.example.projek_mobile_asli

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Onboarding3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding3)

        val btnStart3 = findViewById<Button>(R.id.btnstart3)

        btnStart3.setOnClickListener {
            // TODO: Nanti diarahkan ke halaman Login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}