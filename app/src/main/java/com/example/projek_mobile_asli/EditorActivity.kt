package com.example.projek_mobile_asli

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_mobile_asli.data.AppDatabase
import com.example.projek_mobile_asli.data.entity.User

class EditorActivity : AppCompatActivity() {

    private lateinit var editNama: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var editNoHp: EditText
    private lateinit var btnSimpan: Button
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        // 1. Inisialisasi komponen UI
        editNama = findViewById(R.id.edit_nama)
        editEmail = findViewById(R.id.edit_email)
        editPassword = findViewById(R.id.edit_password)
        editNoHp = findViewById(R.id.edit_no_hp)
        btnSimpan = findViewById(R.id.btn_simpan)

        // 2. Panggil Database
        database = AppDatabase.getInstance(applicationContext)

        // 3. Aksi klik tombol simpan
        btnSimpan.setOnClickListener {
            val nama = editNama.text.toString()
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            val noHp = editNoHp.text.toString()

            // Validasi sederhana
            if (nama.isNotEmpty() && email.isNotEmpty()) {

                // Siapkan data user sesuai tabel di ERD
                val user = User(
                    nama = nama,
                    email = email,
                    password = password,
                    no_hp = noHp,
                    created_at = "2026-07-12" // Bisa diganti dengan format tanggal dinamis nanti
                )

                // Simpan ke database (wajib pakai background thread)
                Thread {
                    database.userDao().insert(user)

                    runOnUiThread {
                        Toast.makeText(this, "Berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        finish() // Menutup halaman Editor dan otomatis kembali ke MainActivity
                    }
                }.start()

            } else {
                Toast.makeText(this, "Nama dan Email wajib diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}