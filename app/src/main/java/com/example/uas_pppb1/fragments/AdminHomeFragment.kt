package com.example.uas_pppb1.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.uas_pppb1.AddArticleActivity
import com.example.uas_pppb1.EditArticleActivity
import com.example.uas_pppb1.R
import com.example.uas_pppb1.adapters.ArticleAdapterAdmin
import com.example.uas_pppb1.data.model.Article
import com.example.uas_pppb1.network.ApiClient
import com.example.uas_pppb1.network.ApiService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminHomeFragment : Fragment() {

    private lateinit var rvArticlesAdmin: RecyclerView
    private lateinit var fabAddArticle: FloatingActionButton
    private lateinit var articleAdapter: ArticleAdapterAdmin
    private val articles = mutableListOf<Article>() // Menyimpan data artikel

    private val editArticleLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val updatedArticle = result.data?.getParcelableExtra<Article>("updatedArticle")
                if (updatedArticle != null) {
                    updateArticleInList(updatedArticle)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_admin_home, container, false)

        rvArticlesAdmin = view.findViewById(R.id.rvArticlesAdmin)
        fabAddArticle = view.findViewById(R.id.fabAddArticle)

        rvArticlesAdmin.layoutManager = LinearLayoutManager(requireContext())
        articleAdapter = ArticleAdapterAdmin(
            articles = articles,
            onDeleteClick = { article -> deleteArticle(article) },
            onEditClick = { article -> navigateToEditArticle(article) }
        )
        rvArticlesAdmin.adapter = articleAdapter

        setupFab()
        loadArticlesFromApi()

        return view
    }

    private fun setupFab() {
        fabAddArticle.setOnClickListener {
            val intent = Intent(requireContext(), AddArticleActivity::class.java)
            startActivity(intent)
        }
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
                        val articlesFromApi = response.body()!!.filter {
                            it.id != null && it.title != null && it.description != null
                        }
                        articles.clear()
                        articles.addAll(articlesFromApi)
                        articleAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "Failed to fetch articles", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "An error occurred: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun deleteArticle(article: Article) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        val database = "vIJIF"
        val table = "articles"

        if (article.id.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Article ID is null or blank!", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.deleteArticle(database, table, article.id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Article deleted successfully", Toast.LENGTH_SHORT).show()
                        articles.remove(article)
                        articleAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "Failed to delete article: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "An error occurred: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun navigateToEditArticle(article: Article) {
        if (article.id.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Invalid article data!", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(requireContext(), EditArticleActivity::class.java).apply {
            putExtra("article", article)
        }
        editArticleLauncher.launch(intent)
    }


    private fun updateArticleInList(updatedArticle: Article) {
        val index = articles.indexOfFirst { it.id == updatedArticle.id }
        if (index != -1) {
            articles[index] = updatedArticle
            articleAdapter.notifyItemChanged(index)
        }
    }
}
