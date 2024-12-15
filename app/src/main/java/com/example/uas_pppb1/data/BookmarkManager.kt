package com.example.uas_pppb1.data

import android.content.Context
import android.content.SharedPreferences
import com.example.uas_pppb1.data.model.Article
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object BookmarkManager {
    private const val PREF_NAME = "BookmarkPrefs"
    private const val KEY_BOOKMARKS = "bookmarked_articles"

    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun addBookmark(article: Article) {
        val bookmarks = getBookmarkedArticles().toMutableList()
        // Pastikan artikel dengan id yang sama belum ada
        if (bookmarks.none { it.id == article.id }) {
            bookmarks.add(article)
        }
        saveBookmarks(bookmarks)
    }

    fun removeBookmark(article: Article) {
        val bookmarks = getBookmarkedArticles().toMutableList()
        bookmarks.removeAll { it.id == article.id }
        saveBookmarks(bookmarks)
    }

    fun isBookmarked(article: Article): Boolean {
        return getBookmarkedArticles().any { it.id == article.id }
    }

    fun getBookmarkedArticles(): List<Article> {
        val json = sharedPreferences.getString(KEY_BOOKMARKS, "[]")
        val type = object : TypeToken<List<Article>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    private fun saveBookmarks(bookmarks: List<Article>) {
        val json = gson.toJson(bookmarks)
        sharedPreferences.edit().putString(KEY_BOOKMARKS, json).apply()
    }
}
