package com.example.projek_mobile_asli

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_mobile_asli.data.AppDatabase
import java.io.File

class ProfileActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var rolePengguna = "user"

    private lateinit var tvNamaProfil: TextView
    private lateinit var ivAvatarFoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        db = AppDatabase.getInstance(this)
        rolePengguna = intent.getStringExtra("ROLE") ?: "user"

        // Inisialisasi View
        tvNamaProfil = findViewById(R.id.tvNamaProfil)
        ivAvatarFoto = findViewById(R.id.ivAvatarFoto)

        // Tombol Edit Profil
        findViewById<Button>(R.id.btnEditProfil).setOnClickListener {
            val intent = Intent(this, EditProfilActivity::class.java)
            startActivity(intent)
        }

        // Tombol Logout
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            confirmLogout()
        }

        // Aktifkan Navbar (Memanggil fungsi dari BottomNavHelper)
        BottomNavHelper.setupBottomNavigation(this, rolePengguna)

        muatProfil()
    }

    private fun confirmLogout() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Apakah Anda yakin ingin keluar dari akun ini?")
            .setPositiveButton("Ya") { _, _ ->
                Toast.makeText(this, "Berhasil Logout", Toast.LENGTH_SHORT).show()
                // Kembali ke LoginActivity
                finishAffinity()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun muatProfil() {
        Thread {
            val profile = db.profileDao().getProfile()
            if (profile != null) {
                runOnUiThread {
                    tvNamaProfil.text = profile.nama
                    if (!profile.fotoPath.isNullOrEmpty() && File(profile.fotoPath).exists()) {
                        ivAvatarFoto.setImageURI(Uri.fromFile(File(profile.fotoPath)))
                    }
                }
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        muatProfil() // Refresh data setiap kali halaman dibuka kembali
    }
}