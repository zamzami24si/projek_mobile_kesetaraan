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
            try {
                if (userRole == "admin" || userRole == "konselor") {
                    // Kalau admin/konselor, buka halaman daftar laporan
                    val intent = Intent(this@DashboardActivity, DaftarLaporanActivity::class.java)
                    intent.putExtra("ROLE", userRole)
                    startActivity(intent)
                } else {
                    // Kalau user biasa, buka halaman form lapor
                    val intent = Intent(this@DashboardActivity, PengaduanActivity::class.java)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                // Tangkap error jika terjadi crash!
                android.widget.Toast.makeText(this@DashboardActivity, "Error Pindah Halaman: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
                android.util.Log.e("DASHBOARD_ERROR", "Gagal pindah halaman", e)
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