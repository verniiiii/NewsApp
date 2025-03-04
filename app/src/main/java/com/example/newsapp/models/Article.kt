package com.example.newsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(
    tableName = "saved_articles",
    primaryKeys = ["url", "owner"] // Составной ключ: уникальность URL + Owner
)
data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String,
    val urlToImage: String?,
    val owner: Int
)

