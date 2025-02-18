package com.example.newsapp.models

data class NewsRespons(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)