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

    // Variabel penampung data dari Dashboard
    private var rolePengguna: String = "user"
    private var namaPengirimAktif: String = "Pengguna"

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

        // 1. Tangkap data dari Intent (Dikirim dari Dashboard)
        rolePengguna = intent.getStringExtra("ROLE") ?: "user"
        namaPengirimAktif = intent.getStringExtra("EXTRA_NAMA_PENGIRIM") ?: "Pengguna"

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
        val id = intent.getLongExtra("EXTRA_ID", -1L)

        Thread {
            val data = db.chatDao().getKonsultasiById(id)

            runOnUiThread {
                if (data == null) {
                    Toast.makeText(this, "Percakapan tidak ditemukan", Toast.LENGTH_SHORT).show()
                    finish()
                    return@runOnUiThread
                }

                konsultasi = data
                tvNamaDetailChat.text = data.namaPengguna

                tvAvatarDetailChat.text = if (data.namaPengguna.isNotEmpty()) data.namaPengguna.take(1).uppercase() else "?"
                tvStatusDetailChat.text = if (data.online) "Online" else "Offline"

                muatPesan()
            }
        }.start()
    }

    private fun muatPesan() {
        val id = konsultasi?.id ?: return

        Thread {
            val daftarPesan = db.chatDao().getPesanByKonsultasi(id)

            runOnUiThread {
                adapter.updateData(daftarPesan)

                if (daftarPesan.isNotEmpty()) {
                    rvPesan.scrollToPosition(daftarPesan.size - 1)
                }
            }
        }.start()
    }

    private fun kirimPesan() {
        val data = konsultasi ?: return
        val isi = etPesan.text.toString().trim()
        if (isi.isEmpty()) return

        val waktuSekarang = SimpleDateFormat("HH:mm", Locale.getDefault()).format(java.util.Date())

        // 2. Tentukan posisi bubble chat (Kiri/Kanan) menggunakan konstanta bawaanmu
        val tipePengirim = if (rolePengguna.lowercase() == "konselor") PesanChat.PENGIRIM_KONSELOR else PesanChat.PENGIRIM_USER

        // 3. Tentukan nama asli yang akan muncul
        val namaYangTertera = if (rolePengguna.lowercase() == "konselor") "Konselor" else namaPengirimAktif

        Thread {
            // Simpan pesan ke database
            db.chatDao().insertPesan(
                PesanChat(
                    konsultasiId = data.id,
                    isiPesan = isi,
                    waktu = waktuSekarang,
                    pengirim = tipePengirim // Tetap pakai ini agar adapter gelembung chat tidak rusak
                )
            )

            // 4. Kirim Notifikasi dengan menyertakan NAMA PENGIRIM
            val penerimaNotif = if (rolePengguna == "user") "konselor" else "user"

            db.notifikasiDao().insert(
                com.example.projek_mobile_asli.data.entity.NotifikasiItem(
                    judul = "Pesan Baru dari $namaYangTertera", // <-- NAMA MUNCUL DI SINI!
                    isi = isi,
                    waktu = waktuSekarang,
                    rolePenerima = penerimaNotif
                )
            )

            // Update waktu, isi pesan terakhir, dan memastikan namaPengguna ter-update dengan nama asli
            val namaTerupdate = if (rolePengguna == "user") namaYangTertera else data.namaPengguna

            val diperbarui = data.copy(
                namaPengguna = namaTerupdate, // <-- Memperbarui nama ruang obrolan dengan nama asli
                pesanTerakhir = isi,
                waktuTerakhir = waktuSekarang,
                belumDibaca = 0
            )
            db.chatDao().updateKonsultasi(diperbarui)

            runOnUiThread {
                konsultasi = diperbarui
                // Update tampilan header (opsional jika ingin nama langsung berubah saat itu juga)
                tvNamaDetailChat.text = namaTerupdate
                tvAvatarDetailChat.text = if (namaTerupdate.isNotEmpty()) namaTerupdate.take(1).uppercase() else "?"

                etPesan.setText("")
                muatPesan()
            }
        }.start()
    }
}