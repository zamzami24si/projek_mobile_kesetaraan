package com.example.projek_mobile_asli.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.projek_mobile_asli.data.entity.Konsultasi
import com.example.projek_mobile_asli.data.entity.PesanChat

@Dao
interface ChatDao {
    // --- Untuk Konsultasi (Daftar Chat) ---
    @Insert
    fun insertKonsultasi(konsultasi: Konsultasi): Long

    @Update
    fun updateKonsultasi(konsultasi: Konsultasi)

    @Query("SELECT * FROM konsultasi WHERE namaPengguna LIKE '%' || :keyword || '%' ORDER BY id DESC")
    fun searchKonsultasi(keyword: String): List<Konsultasi>

    @Query("SELECT * FROM konsultasi WHERE id = :id LIMIT 1")
    fun getKonsultasiById(id: Long): Konsultasi?

    // --- Untuk Pesan ---
    @Insert
    fun insertPesan(pesan: PesanChat)

    @Query("SELECT * FROM pesan_chat WHERE konsultasiId = :konsultasiId ORDER BY id ASC")
    fun getPesanByKonsultasi(konsultasiId: Long): List<PesanChat>
}