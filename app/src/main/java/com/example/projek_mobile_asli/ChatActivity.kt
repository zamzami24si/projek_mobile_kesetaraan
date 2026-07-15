package com.example.projek_mobile_asli

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_mobile_asli.data.AppDatabase
import com.example.projek_mobile_asli.data.entity.Konsultasi

class ChatActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var adapter: KonsultasiAdapter
    private lateinit var rvKonsultasi: RecyclerView

    // Variabel untuk menyimpan jabatan yang login (biasanya konselor)
    private var rolePengguna: String = "konselor"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        db = AppDatabase.getInstance(this)
        rvKonsultasi = findViewById(R.id.rvKonsultasi)

        // Tangkap ROLE dari Dashboard agar tidak hilang
        rolePengguna = intent.getStringExtra("ROLE") ?: "konselor"

        // --- PERBAIKAN TOMBOL BACK ---
        try {
            // Pastikan ID di activity_chat.xml benar-benar "btnBackChat"
            // Jika di XML namanya "btnBack", ganti tulisan R.id.btnBackChat menjadi R.id.btnBack
            val btnBack = findViewById<ImageView>(R.id.btnBackChat)
            btnBack.setOnClickListener {
                finish() // Menutup halaman
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error Tombol Back: Pastikan ID di XML sesuai!", Toast.LENGTH_SHORT).show()
        }

        // Setup Adapter
        adapter = KonsultasiAdapter(emptyList()) { konsultasi ->
            val intent = Intent(this, DetailChatActivity::class.java)
            intent.putExtra("EXTRA_ID", konsultasi.id)
            intent.putExtra("ROLE", rolePengguna) // <-- PENTING: Bawa role konselor ke dalam obrolan!
            startActivity(intent)
        }

        rvKonsultasi.layoutManager = LinearLayoutManager(this)
        rvKonsultasi.adapter = adapter

        // Setup Search Bar
        val etSearch = findViewById<EditText>(R.id.etSearchChat)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                muatKonsultasi(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        muatKonsultasi("")
    }

    private fun muatKonsultasi(keyword: String) {
        Thread {
            val hasil = db.chatDao().searchKonsultasi(keyword)
            runOnUiThread { adapter.updateData(hasil) }
        }.start()
    }
}