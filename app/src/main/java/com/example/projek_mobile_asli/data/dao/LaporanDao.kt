package com.example.projek_mobile_asli.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update // INI YANG KETINGGALAN TADI!
import com.example.projek_mobile_asli.data.entity.Laporan

@Dao
interface LaporanDao {
    @Insert
    fun insertLaporan(laporan: Laporan)

    @Query("SELECT * FROM laporan")
    fun getAllLaporan(): List<Laporan>

    @Delete
    fun deleteLaporan(laporan: Laporan)

    @Update
    fun updateLaporan(laporan: Laporan)
}