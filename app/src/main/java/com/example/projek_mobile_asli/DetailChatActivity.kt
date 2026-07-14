package com.example.projek_mobile_asli

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_mobile_asli.data.AppDatabase
import com.example.projek_mobile_asli.data.entity.Konsultasi
import com.example.projek_mobile_asli.data.entity.PesanChat
import java.text.SimpleDateFormat
import java.util.Locale

class DetailChatActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var adapter: PesanChatAdapter
    private var konsultasi: Konsultasi? = null

    // Komponen UI
    private lateinit var rvPesan: RecyclerView
    private lateinit var etPesan: EditText
    private lateinit var tvNamaDetailChat: TextView
    private lateinit var tvAvatarDetailChat: TextView
    private lateinit var tvStatusDetailChat: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_chat)

        db = AppDatabase.getInstance(this)

        // Hubungkan dengan ID di XML
        rvPesan = findViewById(R.id.rvPesan)
        etPesan = findViewById(R.id.etPesan)
        tvNamaDetailChat = findViewById(R.id.tvNamaDetailChat)
        tvAvatarDetailChat = findViewById(R.id.tvAvatarDetailChat)
        tvStatusDetailChat = findViewById(R.id.tvStatusDetailChat)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnKirim = findViewById<ImageView>(R.id.btnKirim)

        // Setup Adapter Pesan
        adapter = PesanChatAdapter(emptyList())
        rvPesan.layoutManager = LinearLayoutManager(this)
        rvPesan.adapter = adapter

        // Aksi Tombol
        btnBack.setOnClickListener { finish() }
        btnKirim.setOnClickListener { kirimPesan() }

        muatKonsultasi()
    }

    private fun muatKonsultasi() {
        // Ambil ID dari ChatActivity
        val id = intent.getLongExtra("EXTRA_ID", -1L)

        Thread {
            // Ambil data dari Room (ChatDao)
            val data = db.chatDao().getKonsultasiById(id)

            runOnUiThread {
                if (data == null) {
                    Toast.makeText(this, "Percakapan tidak ditemukan", Toast.LENGTH_SHORT).show()
                    finish()
                    return@runOnUiThread
                }

                konsultasi = data
                tvNamaDetailChat.text = data.namaPengguna

                // Ambil huruf pertama untuk foto profil (Avatar)
                tvAvatarDetailChat.text = if (data.namaPengguna.isNotEmpty()) data.namaPengguna.take(1).uppercase() else "?"

                tvStatusDetailChat.text = if (data.online) "Online" else "Offline"

                muatPesan()
            }
        }.start()
    }

    private fun muatPesan() {
        val id = konsultasi?.id ?: return

        Thread {
            // 1. daftarPesan dibuat di dalam Thread
            val daftarPesan = db.chatDao().getPesanByKonsultasi(id)

            // 2. runOnUiThread WAJIB berada di dalam kurung kurawal Thread
            // agar bisa membaca daftarPesan di atasnya
            runOnUiThread {
                adapter.updateData(daftarPesan) // Baris 91

                // Gulir otomatis ke pesan paling bawah (terbaru)
                if (daftarPesan.isNotEmpty()) { // Baris 93
                    rvPesan.scrollToPosition(daftarPesan.size - 1) // Baris 94
                }
            }
        }.start()
    }

    private fun kirimPesan() {
        val data = konsultasi ?: return
        val isi = etPesan.text.toString().trim()
        if (isi.isEmpty()) return

        val waktuSekarang = SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date())

        // Cek siapa yang masuk ke ruangan ini dari Intent
        val role = intent.getStringExtra("ROLE") ?: "user"
        val tipePengirim = if (role.lowercase() == "konselor") PesanChat.PENGIRIM_KONSELOR else PesanChat.PENGIRIM_USER

        Thread {
            // 1. Simpan pesan dengan pengirim yang sesuai (User/Konselor)
            db.chatDao().insertPesan(
                PesanChat(
                    konsultasiId = data.id,
                    isiPesan = isi,
                    waktu = waktuSekarang,
                    pengirim = tipePengirim // <-- INI YANG BERUBAH
                )
            )

            // 2. Update waktu dan isi pesan terakhir di daftar depan
            val diperbarui = data.copy(pesanTerakhir = isi, waktuTerakhir = waktuSekarang, belumDibaca = 0)
            db.chatDao().updateKonsultasi(diperbarui)

            runOnUiThread {
                konsultasi = diperbarui
                etPesan.setText("")
                muatPesan()
            }
        }.start()
    }
}