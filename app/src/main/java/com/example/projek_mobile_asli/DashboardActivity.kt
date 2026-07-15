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
            if (userRole.lowercase() == "konselor") {
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra("ROLE", userRole)
                startActivity(intent)
            } else {
                // User biasa
                Thread {
                    val db = AppDatabase.getInstance(this)

                    // 1. Tentukan ID unik berdasarkan role (Sama persis seperti di EditProfilActivity)
                    val uniqueProfileId = when (userRole.lowercase()) {
                        "admin" -> 100
                        "konselor" -> 200
                        else -> 300
                    }

                    // 2. Ambil data profil menggunakan getProfileById
                    val profile = db.profileDao().getProfileById(uniqueProfileId)
                    val namaUser = profile?.nama ?: "Pengguna"

                    val riwayatChat = db.chatDao().searchKonsultasi(namaUser)
                    val idKonsultasi = if (riwayatChat.isNotEmpty()) {
                        riwayatChat[0].id
                    } else {
                        db.chatDao().insertKonsultasi(Konsultasi(namaPengguna = namaUser, pesanTerakhir = "Belum ada pesan", waktuTerakhir = "", online = true))
                    }
                    runOnUiThread {
                        val intent = Intent(this, DetailChatActivity::class.java)
                        intent.putExtra("EXTRA_ID", idKonsultasi)
                        intent.putExtra("ROLE", userRole)
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