package com.example.projek_mobile_asli

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_mobile_asli.data.AppDatabase

class NotifikasiActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var rolePengguna = "user"

    private lateinit var rvNotifikasi: RecyclerView
    private lateinit var tvEmptyNotif: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifikasi)

        // Karena di NotifikasiActivity kamu menyimpannya di variabel rolePengguna
        BottomNavHelper.setupBottomNavigation(this, rolePengguna)

        db = AppDatabase.getInstance(this)

        // Tangkap siapa yang sedang login
        rolePengguna = intent.getStringExtra("ROLE")?.lowercase() ?: "user"

        rvNotifikasi = findViewById(R.id.rvNotifikasi)
        tvEmptyNotif = findViewById(R.id.tvEmptyNotif)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }
        rvNotifikasi.layoutManager = LinearLayoutManager(this)

        // Aktifkan Navbar di halaman Notifikasi
        BottomNavHelper.setupBottomNavigation(this, rolePengguna)

        muatNotifikasi()
    }

    private fun muatNotifikasi() {
        Thread {
            // Hanya ambil notifikasi untuk jabatannya atau yang untuk "semua"
            val daftar = db.notifikasiDao().getByRole(rolePengguna)

            runOnUiThread {
                rvNotifikasi.adapter = NotifikasiAdapter(daftar)
                tvEmptyNotif.visibility = if (daftar.isEmpty()) View.VISIBLE else View.GONE
            }

            // Tandai semua sudah dibaca setelah ditampilkan di layar
            daftar.filter { !it.sudahDibaca }.forEach {
                db.notifikasiDao().update(it.copy(sudahDibaca = true))
            }
        }.start()
    }
}