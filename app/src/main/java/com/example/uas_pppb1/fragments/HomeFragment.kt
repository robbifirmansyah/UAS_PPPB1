package com.example.uas_pppb1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_pppb1.R
import com.example.uas_pppb1.adapters.ArticleAdapterUser
import com.example.uas_pppb1.data.BookmarkManager
import com.example.uas_pppb1.data.model.Article
import com.example.uas_pppb1.network.ApiClient
import com.example.uas_pppb1.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var rvArticles: RecyclerView
    private lateinit var articleAdapter: ArticleAdapterUser
    private val articles = mutableListOf<Article>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        rvArticles = view.findViewById(R.id.rvArticles)
        rvArticles.layoutManager = LinearLayoutManager(requireContext())
        articleAdapter = ArticleAdapterUser(
            articles = articles,
            onBookmarkClick = { article -> toggleBookmark(article) }
        )
        rvArticles.adapter = articleAdapter

        // Muat data artikel dari API
        loadArticlesFromApi()

        return view
    }

    override fun onResume() {
        super.onResume()
        // Jika dirasa perlu, Anda dapat memanggil syncBookmarkStates di sini.
        // Namun, jika menyebabkan masalah, pertimbangkan untuk menghapusnya.
        syncBookmarkStates()
        articleAdapter.notifyDataSetChanged()
    }

    private fun loadArticlesFromApi() {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        val database = "vIJIF"
        val table = "articles"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getAllArticles(database, table)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        articles.clear()
                        articles.addAll(response.body()!!)
                        // Sinkronisasi status bookmark setelah data dimuat
                        syncBookmarkStates()
                        articleAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "Failed to fetch articles: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "An error occurred: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }
        }
    }

    private fun toggleBookmark(article: Article) {
        if (BookmarkManager.isBookmarked(article)) {
            BookmarkManager.removeBookmark(article)
        } else {
            BookmarkManager.addBookmark(article)
        }
        syncBookmarkStates()
        articleAdapter.notifyDataSetChanged()
    }

    private fun syncBookmarkStates() {
        // Update isBookmarked berdasarkan BookmarkManager
        articles.forEach { it.isBookmarked = BookmarkManager.isBookmarked(it) }
    }
}
