package com.example.projek_mobile_asli.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "laporan")
data class Laporan(
    @PrimaryKey(autoGenerate = true) val id_laporan: Int = 0,
    val judul: String,
    val deskripsi: String,
    val nama_pengirim: String,
    val is_anonim: Boolean,
    val foto_bukti: String?, // Bisa kosong (null) jika user tidak menyertakan foto
    val status: String = "Menunggu"
)