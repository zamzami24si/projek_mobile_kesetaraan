package com.example.projek_mobile_asli

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_mobile_asli.data.AppDatabase
import com.example.projek_mobile_asli.data.entity.Konsultasi

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // 1. Ambil data ROLE yang konsisten
        val userRole = intent.getStringExtra("ROLE") ?: "user"
        Toast.makeText(this, "Login sebagai: $userRole", Toast.LENGTH_SHORT).show()

        // 2. Setup Navbar (Sudah otomatis membawa userRole)
        BottomNavHelper.setupBottomNavigation(this, userRole)

        // 3. Inisialisasi Menu-menu Dashboard
        val menuPengaduan = findViewById<LinearLayout>(R.id.menu_pengaduan)
        val menuNotifikasi = findViewById<LinearLayout>(R.id.menu_notifikasi)
        val menuProfil = findViewById<LinearLayout>(R.id.menu_profile)
        val menuChat = findViewById<LinearLayout>(R.id.menu_chat)
        val menuArtikel = findViewById<LinearLayout>(R.id.menu_artikel)
        val menuLembaga = findViewById<LinearLayout>(R.id.menu_lembaga)

        // LOGIKA VISIBILITAS (Admin tidak bisa akses chat)
        if (userRole == "admin") {
            menuChat.visibility = View.GONE
        } else {
            menuChat.visibility = View.VISIBLE
        }

        // 4. Set Klik Listener
        menuPengaduan.setOnClickListener {
            if (userRole == "admin" || userRole == "konselor") {
                val intent = Intent(this, DaftarLaporanActivity::class.java)
                intent.putExtra("ROLE", userRole)
                startActivity(intent)
            } else {
                val intent = Intent(this, PengaduanActivity::class.java)
                intent.putExtra("ROLE", userRole)
                startActivity(intent)
            }
        }

        menuNotifikasi.setOnClickListener {
            val intent = Intent(this, NotifikasiActivity::class.java)
            intent.putExtra("ROLE", userRole)
            startActivity(intent)
        }

        menuProfil.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("ROLE", userRole) // <--- INI PENTING AGAR ROLE TIDAK HILANG
            startActivity(intent)
        }

        menuChat.setOnClickListener {
            if (userRole == "konselor") {
                // Konselor -> Ke Halaman Daftar Chat
                val intent = Intent(this@DashboardActivity, ChatActivity::class.java)
                intent.putExtra("ROLE", userRole)
                startActivity(intent)
            } else {
                // User -> Ambil nama asli dari Database terlebih dahulu
                Thread {
                    val db = AppDatabase.getInstance(this@DashboardActivity)

                    // 1. AMBIL NAMA ASLI DARI DATABASE PROFIL
                    val profile = db.profileDao().getProfile()
                    val namaUser = profile?.nama ?: "Pengguna" // Jika nama kosong, pakai "Pengguna"

                    // Cek riwayat chat berdasarkan nama asli
                    val riwayatChat = db.chatDao().searchKonsultasi(namaUser)
                    val idKonsultasi: Long

                    if (riwayatChat.isNotEmpty()) {
                        idKonsultasi = riwayatChat[0].id
                    } else {
                        // Buat sesi konsultasi baru dengan nama asli user
                        val sesiBaru = Konsultasi(
                            namaPengguna = namaUser,
                            pesanTerakhir = "Belum ada pesan",
                            waktuTerakhir = "",
                            online = true
                        )
                        idKonsultasi = db.chatDao().insertKonsultasi(sesiBaru)
                    }

                    runOnUiThread {
                        val intent = Intent(this@DashboardActivity, DetailChatActivity::class.java)
                        intent.putExtra("EXTRA_ID", idKonsultasi)
                        intent.putExtra("ROLE", userRole)

                        // 2. KIRIM NAMA PENGIRIM KE DETAIL CHAT
                        intent.putExtra("EXTRA_NAMA_PENGIRIM", namaUser)

                        startActivity(intent)
                    }
                }.start()
            }
        }

        menuArtikel.setOnClickListener {
            val intent = Intent(this, ArtikelActivity::class.java)
            intent.putExtra("ROLE", userRole)
            startActivity(intent)
        }

        menuLembaga.setOnClickListener {
            val intent = Intent(this, LembagaBantuanActivity::class.java)
            intent.putExtra("ROLE", userRole)
            startActivity(intent)
        }
    }
}