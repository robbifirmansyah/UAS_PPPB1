package com.example.uas_pppb1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bson.types.ObjectId // Tambahkan import untuk ObjectId

@Entity(tableName = "users")
data class User(
    @PrimaryKey val _id: String = ObjectId().toHexString(), // Gunakan ObjectId sebagai Primary Key
    val username: String,
    val password: String,
    val role: String
)
