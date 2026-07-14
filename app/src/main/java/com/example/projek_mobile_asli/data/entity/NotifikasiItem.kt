package com.example.projek_mobile_asli.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifikasi")
data class NotifikasiItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String,
    val isi: String,
    val waktu: String,
    val sudahDibaca: Boolean = false,
    val rolePenerima: String // "user", "konselor", "admin", atau "semua"
)