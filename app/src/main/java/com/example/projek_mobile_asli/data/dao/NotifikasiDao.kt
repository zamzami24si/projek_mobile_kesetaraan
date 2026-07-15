package com.example.projek_mobile_asli.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.projek_mobile_asli.data.entity.NotifikasiItem

@Dao
interface NotifikasiDao {
    @Insert
    fun insert(notif: NotifikasiItem)

    @Update
    fun update(notif: NotifikasiItem)

    // Mengambil notifikasi khusus untuk jabatannya ATAU yang ditujukan untuk "semua"
    @Query("SELECT * FROM notifikasi WHERE rolePenerima = :role OR rolePenerima = 'semua' ORDER BY id DESC")
    fun getByRole(role: String): List<NotifikasiItem>
}