package com.example.projek_mobile_asli

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_mobile_asli.data.AppDatabase
import com.example.projek_mobile_asli.data.entity.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Panggil database
        database = AppDatabase.getInstance(applicationContext)

        val etNama = findViewById<EditText>(R.id.et_nama_register)
        val etEmail = findViewById<EditText>(R.id.et_email_register)
        val etPassword = findViewById<EditText>(R.id.et_password_register)
        val etNoHp = findViewById<EditText>(R.id.et_nohp_register)

        val btnRegister = findViewById<Button>(R.id.btn_register_submit)
        val tvKeLogin = findViewById<TextView>(R.id.tv_ke_login)

        // Aksi saat tombol Daftar diklik
        btnRegister.setOnClickListener {
            val nama = etNama.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val noHp = etNoHp.text.toString()

            if (nama.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {

                // Siapkan data user baru
                val newUser = User(
                    nama = nama,
                    email = email,
                    password = password,
                    no_hp = noHp,
                    created_at = "2026-07-12" // Dummy tanggal
                )

                // Simpan ke SQLite di background thread
                Thread {
                    database.userDao().insert(newUser)

                    runOnUiThread {
                        Toast.makeText(this, "Pendaftaran Berhasil!", Toast.LENGTH_SHORT).show()
                        // Pindah ke Login setelah sukses
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }.start()

            } else {
                Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show()
            }
        }

        // Teks "Login" diklik untuk kembali
        tvKeLogin.setOnClickListener {
            finish() // Menutup halaman Register dan otomatis kembali ke Login
        }
    }
}