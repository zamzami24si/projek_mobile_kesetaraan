package com.example.projek_mobile_asli

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_mobile_asli.data.AppDatabase
import com.example.projek_mobile_asli.data.entity.UserProfile
import java.io.File
import java.io.FileOutputStream

class EditProfilActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var fotoPathTerpilih: String? = null

    private lateinit var etNama: EditText
    private lateinit var etEmail: EditText
    private lateinit var etNoHp: EditText
    private lateinit var etBio: EditText
    private lateinit var ivAvatarEdit: ImageView

    private var userRole: String = "user"
    private var uniqueProfileId: Int = 1

    // Peluncur Galeri & Penyimpan Foto ke Internal HP
    private val pilihGambarLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            val path = simpanFotoKeInternal(uri)
            if (path != null) {
                fotoPathTerpilih = path
                ivAvatarEdit.setImageURI(Uri.fromFile(File(path)))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)

        db = AppDatabase.getInstance(this)

        // 1. Tangkap parameter ROLE login untuk menentukan ID unik Kamar Database
        userRole = intent.getStringExtra("ROLE") ?: "user"
        uniqueProfileId = when (userRole.lowercase()) {
            "admin" -> 100    // Kamar simpan Admin
            "konselor" -> 200 // Kamar simpan Konselor
            else -> 300       // Kamar simpan User biasa
        }

        // 2. Inisialisasi komponen UI dari XML
        etNama = findViewById(R.id.etNama)
        etEmail = findViewById(R.id.etEmail)
        etNoHp = findViewById(R.id.etNoHp)
        etBio = findViewById(R.id.etBio)
        ivAvatarEdit = findViewById(R.id.ivAvatarEdit)

        // 3. Set OnClickListener Tombol Aksi
        findViewById<ImageView>(R.id.btnBackEdit).setOnClickListener { finish() }
        findViewById<Button>(R.id.btnUbahFoto).setOnClickListener { pilihGambarLauncher.launch("image/*") }
        findViewById<Button>(R.id.btnSimpanProfil).setOnClickListener { simpanProfil() }

        // 4. Muat data lama dari database agar form tidak kosong saat dibuka
        muatDataLama()
    }

    private fun muatDataLama() {
        Thread {
            // Memanggil getProfileById menyesuaikan ID unik kamarnya masing-masing
            val profile = db.profileDao().getProfileById(uniqueProfileId)
            if (profile != null) {
                runOnUiThread {
                    etNama.setText(profile.nama)
                    etEmail.setText(profile.email)
                    etNoHp.setText(profile.noHp)
                    etBio.setText(profile.bio)

                    fotoPathTerpilih = profile.fotoPath
                    if (!profile.fotoPath.isNullOrEmpty() && File(profile.fotoPath).exists()) {
                        ivAvatarEdit.setImageURI(Uri.fromFile(File(profile.fotoPath)))
                    } else {
                        // Pasang gambar default awal jika di DB belum ada file kustom
                        if (userRole.lowercase() == "admin") {
                            // PERBAIKAN DI SINI: ganti profilenav menjadi profilenavq
                            ivAvatarEdit.setImageResource(R.drawable.profilenavq)
                        } else {
                            ivAvatarEdit.setImageResource(R.drawable.ic_user)
                        }
                    }
                }
            } else {
                // Set isi awal berdasarkan role jika datanya benar-benar kosong baru mendaftar
                runOnUiThread {
                    etNama.setText(userRole.replaceFirstChar { it.uppercase() })
                    etEmail.setText(userRole.lowercase() + "@setaraku.com")
                    if (userRole.lowercase() == "admin") {
                        // PERBAIKAN DI SINI: ganti profilenav menjadi profilenavq
                        ivAvatarEdit.setImageResource(R.drawable.profilenavq)
                    } else {
                        ivAvatarEdit.setImageResource(R.drawable.ic_user)
                    }
                }
            }
        }.start()
    }

    private fun simpanFotoKeInternal(sourceUri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(sourceUri) ?: return null

            // Nama file kita buat unik per-akun agar file foto tidak saling menimpa satu sama lain
            val namaFileFoto = "foto_profil_" + userRole.lowercase() + ".jpg"
            val fileTujuan = File(filesDir, namaFileFoto)

            FileOutputStream(fileTujuan).use { output -> inputStream.copyTo(output) }
            inputStream.close()
            fileTujuan.absolutePath
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal menyimpan foto", Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun simpanProfil() {
        val nama = etNama.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val noHp = etNoHp.text.toString().trim()
        val bio = etBio.text.toString().trim()

        if (nama.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Nama dan email wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        Thread {
            // Memasukkan data UserProfile baru secara runtut sesuai urutan constructor entity
            val profilBaru = UserProfile(
                uniqueProfileId,
                nama,
                email,
                noHp,
                bio,
                fotoPathTerpilih
            )

            // Simpan ke database
            db.profileDao().save(profilBaru)

            runOnUiThread {
                Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.start()
    }
}