package com.example.uas_pppb1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uas_pppb1.data.model.Article
import com.example.uas_pppb1.network.ApiClient
import com.example.uas_pppb1.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminHomeViewModel : ViewModel() {

    private val apiService = ApiClient.retrofit.create(ApiService::class.java)
    private val database = "vIJIF"
    private val table = "articles"

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> get() = _articles

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun loadArticles() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getAllArticles(database, table)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        _articles.value = response.body()
                    } else {
                        _errorMessage.value = "Failed to fetch articles: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "An error occurred: ${e.localizedMessage}"
                }
            }
        }
    }

    fun deleteArticle(article: Article) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.deleteArticle(database, table, article.id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Refresh articles after deletion
                        loadArticles()
                    } else {
                        _errorMessage.value = "Failed to delete article: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "An error occurred: ${e.localizedMessage}"
                }
            }
        }
    }
}
