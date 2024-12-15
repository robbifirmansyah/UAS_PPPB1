package com.example.uas_pppb1

import android.app.Application
import com.example.uas_pppb1.data.BookmarkManager

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        BookmarkManager.init(this)
    }
}
