package com.example.projek_mobile_asli.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "konselor")
data class Konselor(
    @PrimaryKey(autoGenerate = true) val id_konselor: Int = 0,
    val nama: String?,
    val email: String?,
    val password: String?,
    val spesialisasi: String?
)