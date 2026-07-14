package com.example.projek_mobile_asli

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_mobile_asli.data.AppDatabase
import com.example.projek_mobile_asli.data.entity.LembagaBantuan

class LembagaBantuanActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var adapter: LembagaAdapter
    private var rolePengguna = "user"

    private lateinit var rvLembaga: RecyclerView
    private lateinit var btnTambahLembaga: Button

    // Variabel penampung gambar
    private var uriGambarTerpilih: Uri? = null
    private var ivPreviewDialog: ImageView? = null

    // Peluncur Galeri HP
    private val bukaGaleri = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            uriGambarTerpilih = uri
            // Izin permanen agar aplikasi tetap bisa membaca gambar setelah aplikasi ditutup
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            ivPreviewDialog?.setImageURI(uri) // Tampilkan di dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lembaga_bantuan)

        db = AppDatabase.getInstance(this)
        rolePengguna = intent.getStringExtra("ROLE") ?: "user"

        rvLembaga = findViewById(R.id.rvLembaga)
        btnTambahLembaga = findViewById(R.id.btnTambahLembaga)
        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

        if (rolePengguna.lowercase() == "user") {
            btnTambahLembaga.visibility = View.GONE
        }

        btnTambahLembaga.setOnClickListener { tampilkanDialogTambah() }
        rvLembaga.layoutManager = LinearLayoutManager(this)
        muatLembaga()
    }

    private fun tampilkanDialogTambah() {
        uriGambarTerpilih = null // Reset gambar saat form baru dibuka

        val dialogView = layoutInflater.inflate(R.layout.dialog_tambah_lembaga, null)
        val etNama = dialogView.findViewById<EditText>(R.id.etNamaLembaga)
        val etNomor = dialogView.findViewById<EditText>(R.id.etNomorLembaga)
        val etKeterangan = dialogView.findViewById<EditText>(R.id.etKeteranganLembaga)
        val btnPilihGambar = dialogView.findViewById<Button>(R.id.btnPilihGambar)

        ivPreviewDialog = dialogView.findViewById(R.id.ivPreviewGambar)

        // Buka galeri saat tombol diklik
        btnPilihGambar.setOnClickListener {
            bukaGaleri.launch("image/*") // Ambil file bertipe gambar
        }

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setNegativeButton("Batal", null)
            .setPositiveButton("Simpan") { _, _ ->
                val nama = etNama.text.toString().trim()
                val nomor = etNomor.text.toString().trim()
                val ket = etKeterangan.text.toString().trim()
                val stringGambar = uriGambarTerpilih?.toString() ?: ""

                if (nama.isEmpty()) {
                    Toast.makeText(this, "Nama lembaga wajib diisi", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                Thread {
                    db.lembagaDao().insert(
                        LembagaBantuan(
                            namaLembaga = nama,
                            keterangan = ket,
                            nomorTelepon = nomor.ifEmpty { "-" },
                            gambarUri = stringGambar
                        )
                    )
                    db.notifikasiDao().insert(
                        com.example.projek_mobile_asli.data.entity.NotifikasiItem(
                            judul = "Lembaga Baru Ditambahkan!",
                            isi = "Kontak lembaga '$nama' telah berhasil ditambahkan ke dalam daftar.",
                            waktu = "Baru saja",
                            rolePenerima = "semua" // <-- Semua orang (User, Admin, Konselor) akan dapat notif ini!
                        )
                    )
                    runOnUiThread { muatLembaga() }
                }.start()
            }
            .show()
    }

    private fun muatLembaga() {
        Thread {
            val daftar = db.lembagaDao().getAll()
            runOnUiThread {
                adapter = LembagaAdapter(daftar, rolePengguna,
                    onUbahStatus = { lembaga, statusBaru ->
                        Thread {
                            db.lembagaDao().update(lembaga.copy(status = statusBaru))
                            runOnUiThread { muatLembaga() }
                        }.start()
                    },
                    onHapus = { lembaga ->
                        Thread {
                            db.lembagaDao().delete(lembaga)
                            runOnUiThread { muatLembaga() }
                        }.start()
                    }
                )
                rvLembaga.adapter = adapter
            }
        }.start()
    }
}