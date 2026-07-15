package com.example.projek_mobile_asli

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_mobile_asli.data.AppDatabase
import com.example.projek_mobile_asli.data.entity.Artikel


class ArtikelActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var adapter: ArtikelAdapter
    private var rolePengguna = "user"

    // UI Elements
    private lateinit var rvArtikel: RecyclerView
    private lateinit var tvEmptyArtikel: TextView
    private lateinit var btnTambahArtikel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artikel)

        // 1. Ambil Role dari Dashboard
        rolePengguna = intent.getStringExtra("ROLE") ?: "user"

        db = AppDatabase.getInstance(applicationContext)

        rvArtikel = findViewById(R.id.rvArtikel)
        tvEmptyArtikel = findViewById(R.id.tvEmptyArtikel)
        btnTambahArtikel = findViewById(R.id.btnTambahArtikel)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        // 2. Logika Sembunyikan Tombol Tambah
        if (rolePengguna.lowercase() == "admin") {
            btnTambahArtikel.visibility = View.VISIBLE
        } else {
            btnTambahArtikel.visibility = View.GONE
        }

        btnBack.setOnClickListener { finish() }
        btnTambahArtikel.setOnClickListener { tampilkanDialogTambah() }

        rvArtikel.layoutManager = LinearLayoutManager(this)

        muatArtikel()
    }

    private fun tampilkanDialogTambah() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_tambah_artikel, null)
        val etJudul = dialogView.findViewById<EditText>(R.id.etJudulArtikel)
        val etIsi = dialogView.findViewById<EditText>(R.id.etIsiArtikel)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setNegativeButton("Batal", null)
            .setPositiveButton("Tambahkan") { _, _ ->
                val judul = etJudul.text.toString().trim()
                val isi = etIsi.text.toString().trim()

                if (judul.isEmpty() || isi.isEmpty()) {
                    Toast.makeText(this, "Judul dan isi wajib diisi", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                Thread {
                    db.artikelDao().insert(Artikel(judul = judul, isi = isi, waktu = "Baru saja"))
                    runOnUiThread { muatArtikel() }
                }.start()
            }
            .show()
    }

    private fun muatArtikel() {
        Thread {
            val daftar = db.artikelDao().getAll()
            runOnUiThread {
                // 3. Masukkan rolePengguna ke dalam Adapter
                adapter = ArtikelAdapter(daftar, rolePengguna) { artikel ->
                    hapusArtikel(artikel)
                }
                rvArtikel.adapter = adapter
                tvEmptyArtikel.visibility = if (daftar.isEmpty()) View.VISIBLE else View.GONE
            }
        }.start()
    }

    private fun hapusArtikel(artikel: Artikel) {
        Thread {
            db.artikelDao().delete(artikel)
            runOnUiThread {
                Toast.makeText(this, "Artikel dihapus", Toast.LENGTH_SHORT).show()
                muatArtikel()
            }
        }.start()
    }
}