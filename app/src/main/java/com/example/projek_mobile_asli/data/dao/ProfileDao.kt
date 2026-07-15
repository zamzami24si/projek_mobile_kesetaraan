package com.example.projek_mobile_asli.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projek_mobile_asli.data.entity.UserProfile

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(profile: UserProfile)

    // Kita ubah pencariannya dinamis menggunakan pencarian ID unik berdasarkan kode hash peran akun
    @Query("SELECT * FROM user_profile WHERE id = :profileId LIMIT 1")
    fun getProfileById(profileId: Int): UserProfile?
}