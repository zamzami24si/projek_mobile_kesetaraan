package com.example.projek_mobile_asli

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_mobile_asli.adapter.UserAdapter
import com.example.projek_mobile_asli.data.AppDatabase
import com.example.projek_mobile_asli.data.entity.User
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var database: AppDatabase
    private lateinit var userAdapter: UserAdapter
    private var listUser = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Inisialisasi Komponen UI
        recyclerView = findViewById(R.id.recycler_view)
        fabAdd = findViewById(R.id.fab_add)

        // 2. Inisialisasi Database Room
        database = AppDatabase.getInstance(applicationContext)

        // 3. Setup RecyclerView
        userAdapter = UserAdapter(listUser)
        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 4. Aksi saat tombol Tambah diklik (Pindah ke EditorActivity)
        fabAdd.setOnClickListener {
            startActivity(Intent(this, EditorActivity::class.java))
        }
    }

    // Fungsi ini akan dipanggil setiap kali halaman ini muncul di layar
    override fun onResume() {
        super.onResume()
        getDataFromDatabase()
    }

    private fun getDataFromDatabase() {
        // Room tidak mengizinkan query di Main Thread, jadi kita pakai background Thread
        Thread {
            val users = database.userDao().getAll() // Ambil semua data user [cite: 164-184]

            // Update UI harus dikembalikan ke Main Thread
            runOnUiThread {
                listUser.clear()
                listUser.addAll(users)
                userAdapter.notifyDataSetChanged() // Render ulang daftarnya
            }
        }.start()
    }
}