package com.example.projek_mobile_asli

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_mobile_asli.data.AppDatabase
import com.example.projek_mobile_asli.data.entity.Laporan

class DaftarLaporanActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var adapter: LaporanAdapter
    private lateinit var rvLaporan: RecyclerView
    private var rolePengguna = "user"

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_daftar_laporan)

            rolePengguna = intent.getStringExtra("ROLE") ?: "user"
            database = AppDatabase.getInstance(applicationContext)

            // Setup RecyclerView
            rvLaporan = findViewById(R.id.rv_laporan)
            rvLaporan.layoutManager = LinearLayoutManager(this)

            // Setup Tombol Back
            val btnBack = findViewById<android.view.View>(R.id.btn_back)
            btnBack.setOnClickListener { finish() }

            loadDataLaporan()

        } catch (e: Exception) {
            // JIKA CRASH, TANGKAP ERRORNYA DI SINI!
            android.util.Log.e("CRASH_DAFTAR", "Error: ", e)
            android.widget.Toast.makeText(this, "CRASH: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
            finish() // Langsung kembalikan ke Dashboard agar aplikasi tidak terlempar ke layar Login
        }
    }

    private fun loadDataLaporan() {
        Thread {
            val daftarLaporan = database.laporanDao().getAllLaporan()

            runOnUiThread {
                adapter = LaporanAdapter(
                    daftarLaporan,
                    rolePengguna,
                    onStatusClick = { laporan -> tampilkanDialogUbahStatus(laporan) },
                    onDeleteClick = { laporan -> hapusLaporan(laporan) }
                )
                rvLaporan.adapter = adapter
            }
        }.start()
    }

    private fun tampilkanDialogUbahStatus(laporan: Laporan) {
        val pilihanStatus = arrayOf("Menunggu", "Dilaporkan", "Diproses", "Selesai")

        AlertDialog.Builder(this)
            .setTitle("Ubah Status Laporan")
            .setItems(pilihanStatus) { _, which ->
                val statusBaru = pilihanStatus[which]
                val laporanUpdate = laporan.copy(status = statusBaru)

                Thread {
                    database.laporanDao().updateLaporan(laporanUpdate)
                    val dataTerbaru = database.laporanDao().getAllLaporan()

                    runOnUiThread {
                        adapter.updateData(dataTerbaru)
                        Toast.makeText(this, "Status diubah ke: $statusBaru", Toast.LENGTH_SHORT).show()
                    }
                }.start()
            }
            .show()
    }

    private fun hapusLaporan(laporan: Laporan) {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Yakin ingin menghapus laporan ini?")
            .setPositiveButton("Hapus") { _, _ ->
                Thread {
                    database.laporanDao().deleteLaporan(laporan)
                    val dataTerbaru = database.laporanDao().getAllLaporan()

                    runOnUiThread {
                        adapter.updateData(dataTerbaru)
                        Toast.makeText(this, "Laporan dihapus", Toast.LENGTH_SHORT).show()
                    }
                }.start()
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}