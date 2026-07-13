package com.example.projek_mobile_asli

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_mobile_asli.data.AppDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Panggil database
        database = AppDatabase.getInstance(applicationContext)

        val etEmail = findViewById<EditText>(R.id.et_email_login)
        val etPassword = findViewById<EditText>(R.id.et_password_login)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val tvKeRegister = findViewById<TextView>(R.id.tv_ke_register)

        // Aksi saat tombol Login diklik
        btnLogin.setOnClickListener {
            val emailInput = etEmail.text.toString()
            val passwordInput = etPassword.text.toString()

            if (emailInput.isNotEmpty() && passwordInput.isNotEmpty()) {

                // Cek ke SQLite di background thread dibungkus try-catch agar tidak crash
                Thread {
                    try {
                        // 1. Cek apakah dia User biasa
                        val isUser = database.userDao().login(emailInput, passwordInput)

                        // 2. Cek apakah dia Konselor
                        val isKonselor = database.userDao().loginKonselor(emailInput, passwordInput)

                        // 3. Cek apakah dia Admin
                        val isAdmin = database.userDao().loginAdmin(emailInput, passwordInput)

                        runOnUiThread {
                            if (isUser != null) {
                                Toast.makeText(this@LoginActivity, "Halo ${isUser.nama} (User)", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                                intent.putExtra("ROLE", "user")
                                startActivity(intent)
                                finish()

                            } else if (isKonselor != null) {
                                Toast.makeText(this@LoginActivity, "Halo ${isKonselor.nama} (Konselor)", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                                intent.putExtra("ROLE", "konselor")
                                startActivity(intent)
                                finish()

                            } else if (isAdmin != null) {
                                Toast.makeText(this@LoginActivity, "Halo ${isAdmin.nama} (Admin)", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                                intent.putExtra("ROLE", "admin")
                                startActivity(intent)
                                finish()

                            } else {
                                Toast.makeText(this@LoginActivity, "Email atau Password salah!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        // Jika ada error di database, tangkap di sini agar aplikasi tidak restart
                        Log.e("LOGIN_ERROR", "Terjadi kesalahan database: ${e.message}")
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "Error DB: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }.start()

            } else {
                Toast.makeText(this, "Harap isi Email dan Password!", Toast.LENGTH_SHORT).show()
            }
        }

        // Teks "Daftar" diklik untuk pindah ke halaman Register
        tvKeRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}