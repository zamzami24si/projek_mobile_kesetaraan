package com.example.projek_mobile_asli.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.projek_mobile_asli.data.entity.LembagaBantuan

@Dao
interface LembagaDao {
    @Insert
    fun insert(lembaga: LembagaBantuan)

    @Update
    fun update(lembaga: LembagaBantuan)

    @Delete
    fun delete(lembaga: LembagaBantuan)

    @Query("SELECT * FROM lembaga_bantuan ORDER BY id DESC")
    fun getAll(): List<LembagaBantuan>
}