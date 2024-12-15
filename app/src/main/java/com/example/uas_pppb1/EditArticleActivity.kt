package com.example.uas_pppb1

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uas_pppb1.data.model.Article
import com.example.uas_pppb1.network.ApiClient
import com.example.uas_pppb1.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditArticleActivity : AppCompatActivity() {

    private val apiService = ApiClient.retrofit.create(ApiService::class.java)
    private val databaseName = "vIJIF"
    private val tableName = "articles"

    private lateinit var article: Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_article)

        val etTitle = findViewById<EditText>(R.id.etArticleTitle)
        val etDescription = findViewById<EditText>(R.id.etArticleDescription)
        val etMinuteRead = findViewById<EditText>(R.id.etArticleMinuteRead)
        val etAuthor = findViewById<EditText>(R.id.etArticleAuthor)
        val etCover = findViewById<EditText>(R.id.etArticleCover)
        val btnSave = findViewById<Button>(R.id.btnSaveArticle)

        // Ambil Parcelable dengan cara terbaru
        article = intent.extras?.getParcelable<Article>("article", Article::class.java) ?: run {
            Toast.makeText(this, "Article data is missing!", Toast.LENGTH_SHORT).show()
            Log.e("EditArticleActivity", "No article data found in intent")
            finish()
            return
        }

        // Debug log
        Log.d("EditArticleActivity", "Received article: $article")

        // Isi field dengan data artikel
        etTitle.setText(article.title)
        etDescription.setText(article.description)
        etMinuteRead.setText(article.minuteRead.toString())
        etAuthor.setText(article.author)
        etCover.setText(article.cover)

        btnSave.setOnClickListener {
            val updatedArticle = article.copy(
                title = etTitle.text.toString().trim(),
                description = etDescription.text.toString().trim(),
                minuteRead = etMinuteRead.text.toString().toIntOrNull() ?: 0,
                author = etAuthor.text.toString().trim(),
                cover = etCover.text.toString().trim()
            )
            updateArticle(updatedArticle)
        }
    }

    private fun updateArticle(updatedArticle: Article) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.updateArticle(databaseName, tableName, updatedArticle.id, updatedArticle)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val resultIntent = intent.apply {
                            putExtra("updatedArticle", updatedArticle)
                        }
                        setResult(RESULT_OK, resultIntent)
                        Toast.makeText(this@EditArticleActivity, "Article updated successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@EditArticleActivity, "Failed to update article", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EditArticleActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
                Log.e("EditArticleActivity", "Error updating article", e)
            }
        }
    }
}
