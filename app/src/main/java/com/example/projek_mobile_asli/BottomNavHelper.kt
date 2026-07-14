package com.example.projek_mobile_asli

import android.app.Activity
import android.content.Intent
import android.widget.LinearLayout
import android.widget.Toast

object BottomNavHelper {

    // Fungsi ini dipanggil di setiap halaman yang punya navigasi bawah
    // Kita tambahkan parameter 'userRole' agar jabatan tidak hilang saat pindah halaman
    fun setupBottomNavigation(activity: Activity, userRole: String = "user") {
        val navHome = activity.findViewById<LinearLayout>(R.id.nav_home)
        val navProfil = activity.findViewById<LinearLayout>(R.id.nav_profil)
        val navNotifikasi = activity.findViewById<LinearLayout>(R.id.nav_notifikasi)
        val navMenu = activity.findViewById<LinearLayout>(R.id.nav_menu)

        // 1 & 4. HOME dan MENU -> Mengarah ke Dashboard
        val keDashboard = {
            if (activity !is DashboardActivity) {
                val intent = Intent(activity, DashboardActivity::class.java)
                intent.putExtra("ROLE", userRole) // Bawa jabatannya
                // Bersihkan tumpukan halaman agar tombol "Back" HP tidak bikin error
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity.startActivity(intent)
            } else {
                // Jika sudah di Dashboard, tidak perlu pindah
                Toast.makeText(activity, "Anda sudah berada di halaman utama", Toast.LENGTH_SHORT).show()
            }
        }

        navHome?.setOnClickListener { keDashboard() }
        navMenu?.setOnClickListener { keDashboard() }

        // 2. PROFIL -> Mengarah ke Halaman Profile
        navProfil?.setOnClickListener {
            try {
                // Pastikan kamu sudah membuat file ProfileActivity.kt
                // Jika belum, baris ini akan error merah, silakan jadikan komentar dulu (//)
                val intent = Intent(activity, Class.forName("com.example.projek_mobile_asli.ProfileActivity"))
                intent.putExtra("ROLE", userRole)
                activity.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(activity, "Halaman Profile sedang dalam tahap pengembangan!", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. NOTIFIKASI -> Mengarah ke Halaman Notifikasi
        navNotifikasi?.setOnClickListener {
            if (activity !is NotifikasiActivity) {
                val intent = Intent(activity, NotifikasiActivity::class.java)
                intent.putExtra("ROLE", userRole)
                activity.startActivity(intent)
            } else {
                Toast.makeText(activity, "Anda sudah berada di halaman Notifikasi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}