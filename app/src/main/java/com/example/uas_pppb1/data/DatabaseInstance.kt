package com.example.uas_pppb1.data

import android.content.Context
import androidx.room.Room

object DatabaseInstance {
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (instance == null) {
            synchronized(AppDatabase::class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    )
                        .fallbackToDestructiveMigration() // Menghapus database jika skema berubah
                        .build()
                }
            }
        }
        return instance!!
    }
}
