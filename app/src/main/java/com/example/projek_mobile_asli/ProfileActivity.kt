package com.example.projek_mobile_asli

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_mobile_asli.data.AppDatabase
import java.io.File

class ProfileActivity : AppCompatActivity() {

    private lateinit var txtNamaProfil: TextView
    private lateinit var txtEmailProfil: TextView
    private lateinit var txtNoHpProfil: TextView
    private lateinit var txtBioProfil: TextView
    private lateinit var imgFotoProfil: ImageView
    private var userRole: String = "user"
    private var uniqueProfileId: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // 1. Tangkap parameter ROLE login
        userRole = intent.getStringExtra("ROLE") ?: "user"

        // Rumus trik penentu ID unik pembatas database profil berdasarkan string role:
        uniqueProfileId = when (userRole.lowercase()) {
            "admin" -> 100 // Admin dikunci di ID baris 100
            "konselor" -> 200 // Konselor dikunci di ID baris 200
            else -> 300 // User biasa dikunci di ID baris 300
        }

        // 2. Inisialisasi komponen UI dari XML
        txtNamaProfil = findViewById(R.id.txt_nama_profil)
        txtEmailProfil = findViewById(R.id.txt_email_profil)
        txtNoHpProfil = findViewById(R.id.txt_nohp_profil)
        txtBioProfil = findViewById(R.id.txt_bio_profil)
        imgFotoProfil = findViewById(R.id.img_foto_profil)
        val btnBack = findViewById<ImageView>(R.id.btn_back_profile)

        val rowProfilSaya = findViewById<LinearLayout>(R.id.row_profil_saya)
        val btnLogout = findViewById<Button>(R.id.btn_logout)

        btnBack.setOnClickListener {
            finish()
        }

        // Aksi Klik Menu Ubah/Edit Profil
        rowProfilSaya.setOnClickListener {
            val intent = Intent(this, EditProfilActivity::class.java)
            intent.putExtra("ROLE", userRole)
            startActivity(intent)
        }

        // Aksi Klik Tombol Logout
        btnLogout.setOnClickListener {
            Toast.makeText(this, "Berhasil Keluar Akun", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Sambungkan fungsi kontrol Bottom Navbar
        BottomNavHelper.setupBottomNavigation(this, userRole)
    }

    // Jalur Aman Mengambil & Menampilkan Data Informasi Profil Lengkap
    override fun onStart() {
        super.onStart()

        Thread {
            val db = AppDatabase.getInstance(this)
            // Ambil data profil spesifik sesuai ID keunikan akun masing-masing
            val profile = db.profileDao().getProfileById(uniqueProfileId)

            runOnUiThread {
                if (profile != null) {
                    // Set Data Informasi Hasil Inputan Secara Lengkap & Dinamis
                    txtNamaProfil.text = "Hi, ${profile.nama}"
                    txtEmailProfil.text = profile.email
                    txtNoHpProfil.text = if (profile.noHp.isNotEmpty()) profile.noHp else "belum diisi"
                    txtBioProfil.text = if (profile.bio.isNotEmpty()) profile.bio else "belum diisi"

                    // Validasi Pemasangan File Foto Profil Kustom dari Galeri Internal HP
                    if (!profile.fotoPath.isNullOrEmpty() && File(profile.fotoPath).exists()) {
                        imgFotoProfil.setImageURI(Uri.fromFile(File(profile.fotoPath)))
                    } else {
                        setDefaultAvatar()
                    }
                } else {
                    // Set Tampilan Teks Bawaan (Default Awal) Jika Akun Baru Belum Mengisi Profile
                    txtNamaProfil.text = when (userRole.lowercase()) {
                        "admin" -> "Hi, Admin"
                        "konselor" -> "Hi, Konselor"
                        else -> "Hi, Pengguna"
                    }
                    txtEmailProfil.text = "${userRole.lowercase()}@setaraku.com"
                    txtNoHpProfil.text = "belum diisi"
                    txtBioProfil.text = "belum diisi"
                    setDefaultAvatar()
                }
            }
        }.start()
    }

    private fun setDefaultAvatar() {
        if (userRole.lowercase() == "admin") {
            // PERBAIKAN DI SINI: profilenav menjadi profilenavq
            imgFotoProfil.setImageResource(R.drawable.profilenavq)
        } else {
            imgFotoProfil.setImageResource(R.drawable.ic_user)
        }
    }
}