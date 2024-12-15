package com.example.uas_pppb1.data.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "articles")
data class Article(
    @PrimaryKey
    @NonNull
    @SerializedName("_id") val id: String = "", // Default value jika id null dari API
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("minuteRead") val minuteRead: Int? = 0,
    @SerializedName("cover") val cover: String? = null,
    @SerializedName("author") val author: String? = null,
    @SerializedName("isBookmarked") var isBookmarked: Boolean = false
) : Parcelable
