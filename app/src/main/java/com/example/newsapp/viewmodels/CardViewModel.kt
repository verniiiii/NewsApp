package com.example.newsapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.MainDb
import com.example.newsapp.data.UserPreferences
import com.example.newsapp.models.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

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

    fun getAllArticle(): Flow<List<Article>> = flow {
        val userId = userPreferences.userId.firstOrNull()
        if (userId != null) {
            emitAll(mainDb.dao.getAllByOwner(userId))
        } else {
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    fun deleteArticle(url : String){
        viewModelScope.launch{
            mainDb.dao.deleteArticle(url,userPreferences.userId.first()!!)
        }
    }

    fun updateNote(url: String, note: String) {
        viewModelScope.launch {
            val ownerId = userPreferences.userId.first()!!
            mainDb.dao.updateNote(url, ownerId, note)
        }
    }

}