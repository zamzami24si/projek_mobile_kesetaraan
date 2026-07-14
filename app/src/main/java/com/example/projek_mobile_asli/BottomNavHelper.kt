package com.example.projek_mobile_asli

import android.app.Activity
import android.content.Intent
import android.widget.LinearLayout
import android.widget.Toast

object BottomNavHelper {

    fun setupBottomNavigation(activity: Activity, userRole: String = "user") {
        val navHome = activity.findViewById<LinearLayout>(R.id.nav_home)
        val navProfil = activity.findViewById<LinearLayout>(R.id.nav_profil)
        val navNotifikasi = activity.findViewById<LinearLayout>(R.id.nav_notifikasi)
        val navMenu = activity.findViewById<LinearLayout>(R.id.nav_menu)

        // Tombol Home & Menu: Arahkan ke DashboardActivity
        val keDashboard = {
            if (activity !is DashboardActivity) {
                val intent = Intent(activity, DashboardActivity::class.java)
                intent.putExtra("ROLE", userRole) // Kirim role agar Dashboard tahu siapa yang login
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity.startActivity(intent)
            }
        }

        navHome?.setOnClickListener { keDashboard() }
        navMenu?.setOnClickListener { keDashboard() }

        // Tombol Profil: Arahkan ke ProfileActivity
        navProfil?.setOnClickListener {
            if (activity !is ProfileActivity) {
                val intent = Intent(activity, ProfileActivity::class.java)
                intent.putExtra("ROLE", userRole)
                activity.startActivity(intent)
            }
        }

        // Tombol Notifikasi
        navNotifikasi?.setOnClickListener {
            if (activity !is NotifikasiActivity) {
                val intent = Intent(activity, NotifikasiActivity::class.java)
                intent.putExtra("ROLE", userRole)
                activity.startActivity(intent)
            }
        }
    }
}