package com.example.projek_mobile_asli.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.projek_mobile_asli.data.entity.Admin
import com.example.projek_mobile_asli.data.entity.Konselor
import com.example.projek_mobile_asli.data.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    // --- TAMBAHKAN KODE INI UNTUK LOGIN ---
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    fun login(email: String, password: String): User?

    // --- TAMBAHAN UNTUK ADMIN DAN KONSELOR ---
    @Query("SELECT * FROM admin WHERE email = :email AND password = :password LIMIT 1")
    fun loginAdmin(email: String, password: String): Admin?

    @Query("SELECT * FROM konselor WHERE email = :email AND password = :password LIMIT 1")
    fun loginKonselor(email: String, password: String): Konselor?
    // --------------------------------------

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)
}