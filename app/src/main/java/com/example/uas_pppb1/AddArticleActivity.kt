package com.example.uas_pppb1

import android.app.Activity
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

class AddArticleActivity : AppCompatActivity() {

    private val apiService = ApiClient.retrofit.create(ApiService::class.java)
    private val databaseName = "vIJIF"
    private val tableName = "articles"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_article)

        val etTitle = findViewById<EditText>(R.id.etArticleTitle)
        val etDescription = findViewById<EditText>(R.id.etArticleDescription)
        val etMinuteRead = findViewById<EditText>(R.id.etArticleMinuteRead)
        val etAuthor = findViewById<EditText>(R.id.etArticleAuthor)
        val etCover = findViewById<EditText>(R.id.etArticleCover)
        val btnSave = findViewById<Button>(R.id.btnSaveArticle)

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val minuteRead = etMinuteRead.text.toString().toIntOrNull() ?: 0
            val author = etAuthor.text.toString().trim()
            val cover = etCover.text.toString().trim()

            if (title.isEmpty() || description.isEmpty() || author.isEmpty() || cover.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newArticle = Article(
                id = "TEMP_ID", // ID bisa dikosongkan atau diisi sementara, server biasanya generate
                title = title,
                description = description,
                minuteRead = minuteRead,
                cover = cover,
                author = author
            )

            saveArticle(newArticle)
        }

    }

    private fun saveArticle(article: Article) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("AddArticleActivity", "Sending article data: $article") // Debug data yang dikirim
                val response = apiService.createArticle(databaseName, tableName, article)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddArticleActivity, "Article added successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Log.e("AddArticleActivity", "Failed response: ${response.errorBody()?.string()}")
                        Toast.makeText(this@AddArticleActivity, "Failed to add article", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("AddArticleActivity", "Error: ${e.localizedMessage}", e)
                    Toast.makeText(this@AddArticleActivity, "An error occurred: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
