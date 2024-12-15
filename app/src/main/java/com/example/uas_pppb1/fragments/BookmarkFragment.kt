package com.example.uas_pppb1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_pppb1.R
import com.example.uas_pppb1.adapters.ArticleAdapterUser
import com.example.uas_pppb1.data.BookmarkManager
import com.example.uas_pppb1.data.model.Article

class BookmarkFragment : Fragment() {

    private lateinit var rvBookmarks: RecyclerView
    private lateinit var articleAdapter: ArticleAdapterUser
    private val bookmarkedArticles = mutableListOf<Article>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_bookmark, container, false)

        rvBookmarks = view.findViewById(R.id.rvBookmarks)
        rvBookmarks.layoutManager = LinearLayoutManager(requireContext())
        updateBookmarkedArticles()

        return view
    }

    override fun onResume() {
        super.onResume()
        updateBookmarkedArticles()
    }

    private fun updateBookmarkedArticles() {
        bookmarkedArticles.clear()
        bookmarkedArticles.addAll(BookmarkManager.getBookmarkedArticles())

        if (!::articleAdapter.isInitialized) {
            articleAdapter = ArticleAdapterUser(
                articles = bookmarkedArticles,
                onBookmarkClick = { article -> removeBookmark(article) }
            )
            rvBookmarks.adapter = articleAdapter
        } else {
            articleAdapter.notifyDataSetChanged()
        }
    }

    private fun removeBookmark(article: Article) {
        BookmarkManager.removeBookmark(article)
        updateBookmarkedArticles()
    }
}
