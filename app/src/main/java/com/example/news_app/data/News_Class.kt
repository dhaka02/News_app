package com.example.news_app.data

import com.example.news_app.data.Article

data class News_Class(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)