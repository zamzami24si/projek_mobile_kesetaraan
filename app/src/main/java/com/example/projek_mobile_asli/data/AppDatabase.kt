package com.example.projek_mobile_asli.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projek_mobile_asli.data.dao.ArtikelDao
import com.example.projek_mobile_asli.data.dao.LaporanDao
import com.example.projek_mobile_asli.data.dao.UserDao
import com.example.projek_mobile_asli.data.dao.ChatDao
import com.example.projek_mobile_asli.data.dao.LembagaDao
import com.example.projek_mobile_asli.data.dao.NotifikasiDao
import com.example.projek_mobile_asli.data.entity.Laporan
import com.example.projek_mobile_asli.data.entity.User
import com.example.projek_mobile_asli.data.entity.Admin
import com.example.projek_mobile_asli.data.entity.Konselor
import com.example.projek_mobile_asli.data.entity.Artikel
import com.example.projek_mobile_asli.data.entity.Konsultasi
import com.example.projek_mobile_asli.data.entity.PesanChat
import com.example.projek_mobile_asli.data.entity.LembagaBantuan
import com.example.projek_mobile_asli.data.entity.NotifikasiItem

@Database(entities = [User::class, NotifikasiItem::class, LembagaBantuan::class, Laporan::class, Admin::class, PesanChat::class, Konsultasi::class, Konselor::class, Artikel::class], version = 9)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun laporanDao(): LaporanDao

    abstract fun artikelDao(): ArtikelDao

    abstract fun chatDao(): ChatDao

    abstract fun lembagaDao(): LembagaDao

    abstract fun notifikasiDao(): NotifikasiDao


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