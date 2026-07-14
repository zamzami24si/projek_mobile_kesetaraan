package com.example.projek_mobile_asli

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_mobile_asli.data.AppDatabase
import com.example.projek_mobile_asli.data.entity.Laporan

class LaporanActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private var fotoUriString: String? = null // Menyimpan lokasi foto

    // Fungsi pembuka Galeri
    private val pilihFotoLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            // Memberikan izin akses permanen agar foto tetap bisa dibaca oleh aplikasi nanti
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

            fotoUriString = uri.toString()

            val ivPreview = findViewById<ImageView>(R.id.iv_preview_foto)
            val tvNamaFile = findViewById<TextView>(R.id.tv_nama_file)

            ivPreview.setImageURI(uri)
            ivPreview.visibility = View.VISIBLE // Tampilkan gambarnya
            tvNamaFile.text = "Foto berhasil ditambahkan"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan)

        database = AppDatabase.getInstance(applicationContext)

        val etJudul = findViewById<EditText>(R.id.et_judul_laporan)
        val etDeskripsi = findViewById<EditText>(R.id.et_deskripsi_laporan)
        val cbAnonim = findViewById<CheckBox>(R.id.cb_anonim)
        val btnKirim = findViewById<Button>(R.id.btn_kirim_laporan)
        val btnUploadFoto = findViewById<Button>(R.id.btn_upload_foto)

        // Tombol Pilih Foto ditekan
        btnUploadFoto.setOnClickListener {
            pilihFotoLauncher.launch("image/*") // Buka galeri hanya untuk memilih gambar
        }

        btnKirim.setOnClickListener {
            val judul = etJudul.text.toString().trim()
            val deskripsi = etDeskripsi.text.toString().trim()
            val isAnonim = cbAnonim.isChecked

            val namaUserSaatIni = "Budi (User)" // Dummy data nama

            if (judul.isNotEmpty() && deskripsi.isNotEmpty()) {
                Thread {
                    val laporanBaru = Laporan(
                        judul = judul,
                        deskripsi = deskripsi,
                        nama_pengirim = namaUserSaatIni,
                        is_anonim = isAnonim,
                        foto_bukti = fotoUriString // Menyimpan link foto ke database
                    )
                    database.laporanDao().insertLaporan(laporanBaru)

                    // ==========================================
                    // INI DIA PELATUK NOTIFIKASINYA (POINT 3)
                    // ==========================================
                    database.notifikasiDao().insert(
                        com.example.projek_mobile_asli.data.entity.NotifikasiItem(
                            judul = "Laporan Pengaduan Baru",
                            isi = "Ada pengaduan baru dengan judul '$judul' masuk ke dalam sistem.",
                            waktu = "Baru saja",
                            rolePenerima = "semua" // "semua" berarti Admin, Konselor, dan User akan dapat notifnya
                        )
                    )
                    // ==========================================

                    runOnUiThread {
                        Toast.makeText(this, "Laporan berhasil dikirim!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }.start()
            } else {
                Toast.makeText(this, "Judul dan Detail laporan harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}