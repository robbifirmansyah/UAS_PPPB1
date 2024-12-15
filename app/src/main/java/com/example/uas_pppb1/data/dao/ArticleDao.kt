package com.example.uas_pppb1.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.uas_pppb1.data.model.Article

@Dao
interface ArticleDao {

    @Insert
    fun insertArticle(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): List<Article>
}
