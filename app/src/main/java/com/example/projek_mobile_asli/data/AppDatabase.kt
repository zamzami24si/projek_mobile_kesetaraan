package com.example.projek_mobile_asli.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projek_mobile_asli.data.dao.LaporanDao
import com.example.projek_mobile_asli.data.dao.UserDao
import com.example.projek_mobile_asli.data.entity.Laporan // <-- Ini import yang sebelumnya hilang
import com.example.projek_mobile_asli.data.entity.User
import com.example.projek_mobile_asli.data.entity.Admin
import com.example.projek_mobile_asli.data.entity.Konselor

@Database(entities = [User::class, Laporan::class, Admin::class, Konselor::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun laporanDao(): LaporanDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "peduli_kesetaraan_v3"
                )
                    .fallbackToDestructiveMigration() // Ini kunci supaya tabel dibuat ulang
                    .build().also { instance = it }
            }
        }
    }
}