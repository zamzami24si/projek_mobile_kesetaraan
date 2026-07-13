package com.example.projek_mobile_asli

import android.app.Activity
import android.content.Intent
import android.widget.LinearLayout

object BottomNavHelper {

    // Fungsi ini akan dipanggil di setiap halaman yang punya navigasi
    fun setupBottomNavigation(activity: Activity) {
        val navHome = activity.findViewById<LinearLayout>(R.id.nav_home)
        val navProfil = activity.findViewById<LinearLayout>(R.id.nav_profil)
        // val navNotifikasi = activity.findViewById<LinearLayout>(R.id.nav_notifikasi)
        // val navMenu = activity.findViewById<LinearLayout>(R.id.nav_menu)

        navHome?.setOnClickListener {
            // Cek jika kita belum berada di Dashboard, maka pindah ke Dashboard
            if (activity !is DashboardActivity) {
                val intent = Intent(activity, DashboardActivity::class.java)
                // Bersihkan tumpukan halaman sebelumnya agar tidak menumpuk saat di-back
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity.startActivity(intent)
            }
        }

        navProfil?.setOnClickListener {
            // TODO: Nanti diarahkan ke ProfileActivity
            // if (activity !is ProfileActivity) {
            //     activity.startActivity(Intent(activity, ProfileActivity::class.java))
            // }
        }
    }
}