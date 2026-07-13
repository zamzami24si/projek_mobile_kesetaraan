package com.example.projek_mobile_asli.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id_user: Int = 0,
    val nama: String?,
    val email: String?,
    val password: String?,
    val no_hp: String?,
    val created_at: String?
)
