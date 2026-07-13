package com.example.projek_mobile_asli

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_mobile_asli.data.AppDatabase

class PengaduanActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var rvRiwayat: RecyclerView
    private lateinit var adapter: LaporanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaduan)

        // Aktifkan Navigasi Bawah
        // Pastikan class BottomNavHelper kamu sudah siap
        // BottomNavHelper.setupBottomNavigation(this)

        database = AppDatabase.getInstance(applicationContext)
        rvRiwayat = findViewById(R.id.rv_riwayat_laporan)
        rvRiwayat.layoutManager = LinearLayoutManager(this)

        val btnTambahLaporan = findViewById<Button>(R.id.btn_ke_form_laporan)
        btnTambahLaporan.setOnClickListener {
            startActivity(Intent(this, LaporanActivity::class.java))
        }
    }

    // onResume berjalan setiap kali halaman ini tampil di layar
    override fun onResume() {
        super.onResume()
        loadRiwayatLaporan()
    }

    private fun loadRiwayatLaporan() {
        Thread {
            // Mengambil semua data laporan dari SQLite
            // Catatan: Nanti kalau fitur Sesi Login sudah sempurna,
            // kita bisa filter datanya khusus milik User yang sedang login saja.
            val riwayat = database.laporanDao().getAllLaporan()

            runOnUiThread {
                // Gunakan "user" sebagai role, maka tombol hapus di adapter otomatis disembunyikan
                adapter = LaporanAdapter(
                    riwayat,
                    "user",
                    onStatusClick = {
                        // Dikosongkan karena user tidak boleh ubah status
                    },
                    onDeleteClick = {
                        // Dikosongkan karena user tidak boleh hapus
                    }
                )
                rvRiwayat.adapter = adapter
            }
        }.start()
    }
}