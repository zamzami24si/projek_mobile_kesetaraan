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

    // Peluncur Galeri & Penyimpan Foto ke Internal HP (Kode dari temanmu, ini sangat bagus!)
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

        etNama = findViewById(R.id.etNama)
        etEmail = findViewById(R.id.etEmail)
        etNoHp = findViewById(R.id.etNoHp)
        etBio = findViewById(R.id.etBio)
        ivAvatarEdit = findViewById(R.id.ivAvatarEdit)

        findViewById<ImageView>(R.id.btnBackEdit).setOnClickListener { finish() }
        findViewById<Button>(R.id.btnUbahFoto).setOnClickListener { pilihGambarLauncher.launch("image/*") }
        findViewById<Button>(R.id.btnSimpanProfil).setOnClickListener { simpanProfil() }

        muatDataLama()
    }

    private fun muatDataLama() {
        Thread {
            val profile = db.profileDao().getProfile()
            if (profile != null) {
                runOnUiThread {
                    etNama.setText(profile.nama)
                    etEmail.setText(profile.email)
                    etNoHp.setText(profile.noHp)
                    etBio.setText(profile.bio)

                    fotoPathTerpilih = profile.fotoPath
                    if (!profile.fotoPath.isNullOrEmpty() && File(profile.fotoPath).exists()) {
                        ivAvatarEdit.setImageURI(Uri.fromFile(File(profile.fotoPath)))
                    }
                }
            }
        }.start()
    }

    private fun simpanFotoKeInternal(sourceUri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(sourceUri) ?: return null
            val fileTujuan = File(filesDir, "foto_profil.jpg")
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
            db.profileDao().save(
                UserProfile(nama = nama, email = email, noHp = noHp, bio = bio, fotoPath = fotoPathTerpilih)
            )
            runOnUiThread {
                Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.start()
    }
}