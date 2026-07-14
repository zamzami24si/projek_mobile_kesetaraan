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

        val menuChat = findViewById<LinearLayout>(R.id.menu_chat)

        menuChat.setOnClickListener {
            val role = userRole.lowercase()

            if (role == "admin") {
                // 1. ADMIN DITOLAK
                android.widget.Toast.makeText(this, "Fitur Chat hanya untuk User & Konselor!", android.widget.Toast.LENGTH_SHORT).show()

            } else if (role == "konselor") {
                // 2. KONSELOR -> Ke Halaman Daftar Chat
                val intent = Intent(this@DashboardActivity, ChatActivity::class.java)
                startActivity(intent)

            } else {
                // 3. USER -> Langsung masuk ke Ruang Obrolan
                // Kita gunakan Thread karena harus mencari/membuat ID obrolan di Database dulu
                Thread {
                    val db = com.example.projek_mobile_asli.data.AppDatabase.getInstance(this@DashboardActivity)

                    // Nama default user (bisa kamu ganti dengan variabel nama akun yang login jika ada)
                    val namaUser = "Pengguna (Pasien)"

                    // Cek apakah user ini sudah pernah chat?
                    val riwayatChat = db.chatDao().searchKonsultasi(namaUser)
                    val idKonsultasi: Long

                    if (riwayatChat.isNotEmpty()) {
                        // Kalau sudah ada, ambil ID-nya
                        idKonsultasi = riwayatChat[0].id
                    } else {
                        // Kalau belum ada, buatkan ruang chat baru secara otomatis!
                        val sesiBaru = com.example.projek_mobile_asli.data.entity.Konsultasi(
                            namaPengguna = namaUser,
                            pesanTerakhir = "Belum ada pesan",
                            waktuTerakhir = "",
                            online = true
                        )
                        idKonsultasi = db.chatDao().insertKonsultasi(sesiBaru)
                    }

                    runOnUiThread {
                        val intent = Intent(this@DashboardActivity, DetailChatActivity::class.java)
                        // Bawa ID obrolan dan Jabatannya
                        intent.putExtra("EXTRA_ID", idKonsultasi)
                        intent.putExtra("ROLE", role)
                        startActivity(intent)
                    }
                }.start()
            }
        }

        val menuArtikel = findViewById<LinearLayout>(R.id.menu_artikel)

        menuArtikel.setOnClickListener {
            try {
                val intent = Intent(this@DashboardActivity, ArtikelActivity::class.java)

                // BARIS INI SANGAT PENTING AGAR ARTIKELACTIVITY TAHU JABATANNYA!
                intent.putExtra("ROLE", userRole)

                startActivity(intent)
            } catch (e: Exception) {
                android.widget.Toast.makeText(this@DashboardActivity, "Error Buka Artikel: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
            }
        }

        // Panggil elemen menu konsultasi dari XML
        val menuKonsultasi = findViewById<LinearLayout>(R.id.menu_chat)

        // Tampilkan pesan sapaan sesuai role
        Toast.makeText(this, "Login sebagai: $userRole", Toast.LENGTH_SHORT).show()

        // LOGIKA UTAMA: Sembunyikan menu Konsultasi jika yang login adalah admin
        if (userRole == "admin") {
            menuKonsultasi.visibility = View.GONE
            // View.GONE akan menghilangkan elemen 100% dan membuat ruangnya terisi oleh elemen di bawahnya
        } else {
            menuKonsultasi.visibility = View.VISIBLE
        }

        val menuLembaga = findViewById<LinearLayout>(R.id.menu_lembaga)
        menuLembaga.setOnClickListener {
            val intent = Intent(this, LembagaBantuanActivity::class.java)
            intent.putExtra("ROLE", userRole) // Penting agar halaman tahu siapa yang buka
            startActivity(intent)
        }

        // Aktifkan fungsi klik navigasi dari class helper
        BottomNavHelper.setupBottomNavigation(this)
    }
}