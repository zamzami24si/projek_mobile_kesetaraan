package com.example.projek_mobile_asli

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Onboarding1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Pastikan nama file XML kamu adalah activity_onboarding1.xml
        setContentView(R.layout.activity_onboarding1)

        val btnStart1 = findViewById<Button>(R.id.btnstart1)
        val btnSkip1 = findViewById<Button>(R.id.btnskip1)

        btnStart1.setOnClickListener {
            startActivity(Intent(this, Onboarding2Activity::class.java))
        }

        btnSkip1.setOnClickListener {
            // TODO: Nanti diarahkan ke halaman Login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}