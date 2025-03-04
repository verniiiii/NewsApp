package com.example.newsapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.models.Article
import kotlinx.coroutines.flow.Flow
@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addArticleToDb(article: Article)

    @Query("DELETE FROM saved_articles WHERE url = :url AND owner = :owner")
    suspend fun deleteArticle(url: String, owner: Int)

    @Query("SELECT * FROM saved_articles WHERE owner = :owner")
    fun getAllByOwner(owner: Int): Flow<List<Article>>

    @Query("UPDATE saved_articles SET note = :note WHERE url = :url AND owner = :owner")
    suspend fun updateNote(url: String, owner: Int, note: String)
}