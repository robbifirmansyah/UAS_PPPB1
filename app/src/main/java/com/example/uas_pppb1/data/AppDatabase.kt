package com.example.uas_pppb1.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.uas_pppb1.data.dao.ArticleDao
import com.example.uas_pppb1.data.dao.UserDao
import com.example.uas_pppb1.data.model.Article
import com.example.uas_pppb1.data.model.User

// Daftarkan semua entitas dan DAO di sini
@Database(
    entities = [User::class, Article::class], // Entitas yang digunakan
    version = 4,                             // Pastikan versi database ditingkatkan
    exportSchema = false                     // Tidak perlu ekspor schema (untuk pengembangan)
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao          // DAO untuk User
    abstract fun articleDao(): ArticleDao    // DAO untuk Article
}
