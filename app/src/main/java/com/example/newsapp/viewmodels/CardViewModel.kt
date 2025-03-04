package com.example.newsapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.MainDb
import com.example.newsapp.data.UserPreferences
import com.example.newsapp.models.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CardViewModel (
    private val mainDb: MainDb,
    private val userPreferences: UserPreferences,
) : ViewModel(){

    fun addArticleToDb(article : Article){
        viewModelScope.launch{
            val articleWithOwner = article.copy(owner = userPreferences.userId.first()!!)
            mainDb.dao.addArticleToDb(articleWithOwner)
        }
    }

    fun getAllArticle(): Flow<List<Article>> {
        val ownerId = runBlocking { userPreferences.userId.first()!! } // Получаем userId синхронно
        return mainDb.dao.getAllByOwner(ownerId)
    }

    fun deleteArticle(url : String){
        viewModelScope.launch{
            mainDb.dao.deleteArticle(url,userPreferences.userId.first()!!)
        }
    }



}