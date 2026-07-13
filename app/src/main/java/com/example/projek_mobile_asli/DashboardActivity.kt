package com.example.projek_mobile_asli

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Menerima data "ROLE" yang dikirim dari halaman Login
        val userRole = intent.getStringExtra("ROLE") ?: "user"

        // Panggil menu pengaduan dari XML
        val menuPengaduan = findViewById<LinearLayout>(R.id.menu_pengaduan)

        menuPengaduan.setOnClickListener {
            if (userRole == "admin" || userRole == "konselor") {
                // Kalau admin/konselor, buka halaman daftar
                val intent = Intent(this, DaftarLaporanActivity::class.java)
                intent.putExtra("ROLE", userRole) // Bawa info jabatannya
                startActivity(intent)
            } else {
                // Kalau user biasa, buka halaman bikin laporan
                val intent = Intent(this, PengaduanActivity::class.java)
                startActivity(intent)
            }
        }

        // Panggil elemen menu konsultasi dari XML
        val menuKonsultasi = findViewById<LinearLayout>(R.id.menu_konsultasi)

        // Tampilkan pesan sapaan sesuai role
        Toast.makeText(this, "Login sebagai: $userRole", Toast.LENGTH_SHORT).show()

        // LOGIKA UTAMA: Sembunyikan menu Konsultasi jika yang login adalah admin
        if (userRole == "admin") {
            menuKonsultasi.visibility = View.GONE
            // View.GONE akan menghilangkan elemen 100% dan membuat ruangnya terisi oleh elemen di bawahnya
        } else {
            menuKonsultasi.visibility = View.VISIBLE
        }

        // Aktifkan fungsi klik navigasi dari class helper
        BottomNavHelper.setupBottomNavigation(this)
    }
}