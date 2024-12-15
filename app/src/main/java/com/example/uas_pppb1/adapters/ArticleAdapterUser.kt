package com.example.uas_pppb1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_pppb1.R
import com.example.uas_pppb1.data.model.Article

class ArticleAdapterUser(
    private var articles: List<Article>,
    private val onBookmarkClick: (Article) -> Unit
) : RecyclerView.Adapter<ArticleAdapterUser.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article_user, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int = articles.size

    fun updateArticles(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitleUser)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescriptionUser)
        private val ivBookmark: ImageView = itemView.findViewById(R.id.ivBookmark)

        fun bind(article: Article) {
            tvTitle.text = article.title
            tvDescription.text = article.description

            ivBookmark.setImageResource(
                if (article.isBookmarked) R.drawable.ic_bookmark else R.drawable.ic_bookmark_stroke
            )

            ivBookmark.setOnClickListener {
                onBookmarkClick(article)
            }
        }
    }
}
