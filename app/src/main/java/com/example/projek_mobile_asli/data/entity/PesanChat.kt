package com.example.projek_mobile_asli.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pesan_chat")
data class PesanChat(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val konsultasiId: Long,
    val isiPesan: String,
    val waktu: String,
    val pengirim: String
) {
    companion object {
        const val PENGIRIM_USER = "user"
        const val PENGIRIM_KONSELOR = "konselor"
    }
}