package com.example.projek_mobile_asli.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lembaga_bantuan")
data class LembagaBantuan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val namaLembaga: String,
    val keterangan: String, // KOLOM BARU
    val nomorTelepon: String,
    val gambarUri: String,  // KOLOM BARU (Menyimpan lokasi gambar)
    val status: String = STATUS_OPEN
) {
    companion object {
        const val STATUS_OPEN = "Open"
        const val STATUS_CLOSE = "Close"
    }
}