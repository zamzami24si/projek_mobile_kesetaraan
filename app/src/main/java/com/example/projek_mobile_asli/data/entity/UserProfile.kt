package com.example.projek_mobile_asli.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1, // Kita pakai ID 1 terus agar datanya saling menimpa saat diedit
    val nama: String,
    val email: String,
    val noHp: String,
    val bio: String,
    val fotoPath: String?
)