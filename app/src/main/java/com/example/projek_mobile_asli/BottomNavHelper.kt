package com.example.projek_mobile_asli

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.widget.LinearLayout

object BottomNavHelper {

    fun setupBottomNavigation(activity: Activity, userRole: String = "user") {
        val navHome = activity.findViewById<LinearLayout>(R.id.nav_home)
        val navProfil = activity.findViewById<LinearLayout>(R.id.nav_profil)
        val navNotifikasi = activity.findViewById<LinearLayout>(R.id.nav_notifikasi)
        val navMenu = activity.findViewById<LinearLayout>(R.id.nav_menu)

        // Reset semua background ke transparan sebelum menandai yang aktif
        navHome?.setBackgroundColor(Color.TRANSPARENT)
        navProfil?.setBackgroundColor(Color.TRANSPARENT)
        navNotifikasi?.setBackgroundColor(Color.TRANSPARENT)
        navMenu?.setBackgroundColor(Color.TRANSPARENT)

        // Deteksi halaman dan pasang background abu-abu transparan melengkung
        when (activity) {
            is DashboardActivity -> {
                navHome?.setBackgroundResource(R.drawable.bg_nav_active)
            }
            is ProfileActivity -> {
                navProfil?.setBackgroundResource(R.drawable.bg_nav_active)
            }
            is NotifikasiActivity -> {
                navNotifikasi?.setBackgroundResource(R.drawable.bg_nav_active)
            }
            // Jika nanti ada MenuActivity khusus, bisa ditambahkan di sini
        }

        // Aksi Klik Home & Menu
        val keDashboard = {
            if (activity !is DashboardActivity) {
                val intent = Intent(activity, DashboardActivity::class.java)
                intent.putExtra("ROLE", userRole)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity.startActivity(intent)
            }
        }

        navHome?.setOnClickListener { keDashboard() }
        navMenu?.setOnClickListener { keDashboard() }

        // Aksi Klik Profil
        navProfil?.setOnClickListener {
            if (activity !is ProfileActivity) {
                val intent = Intent(activity, ProfileActivity::class.java)
                intent.putExtra("ROLE", userRole)
                activity.startActivity(intent)
            }
        }

        // Aksi Klik Notifikasi
        navNotifikasi?.setOnClickListener {
            if (activity !is NotifikasiActivity) {
                val intent = Intent(activity, NotifikasiActivity::class.java)
                intent.putExtra("ROLE", userRole)
                activity.startActivity(intent)
            }
        }
    }
}