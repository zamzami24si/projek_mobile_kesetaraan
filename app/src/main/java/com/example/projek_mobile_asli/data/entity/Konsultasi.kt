package com.example.projek_mobile_asli.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "konsultasi")
data class Konsultasi(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val namaPengguna: String,
    val pesanTerakhir: String,
    val waktuTerakhir: String,
    val online: Boolean = false,
    val belumDibaca: Int = 0
)