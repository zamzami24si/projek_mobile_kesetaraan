package com.example.projek_mobile_asli.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.projek_mobile_asli.data.entity.Artikel

@Dao
interface ArtikelDao {
    @Insert
    fun insert(artikel: Artikel)

    @Query("SELECT * FROM artikel ORDER BY id DESC")
    fun getAll(): List<Artikel>

    @Delete
    fun delete(artikel: Artikel)
}