package com.example.newsapp.viewmodels

import NewsApi
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.MainDb
import com.example.newsapp.data.UserPreferences
import com.example.newsapp.models.Article
import com.example.newsapp.models.ConstValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException


class TopNewsViewModel(
    private val newsApi : NewsApi,
    private val mainDb: MainDb,
    private val userPreferences: UserPreferences,
): ViewModel(){

    private val _newsArticleld = MutableStateFlow<List<Article>>(emptyList())
    val newsArticleld : StateFlow<List<Article>> = _newsArticleld

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading : MutableStateFlow<Boolean> = _isLoading

    private val _findnewsArticleld = MutableStateFlow<List<Article>>(emptyList())
    val findnewsArticleld : StateFlow<List<Article>> = _findnewsArticleld


    fun fetchNews(page : Int = 1) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = newsApi.getNews(apiKey = ConstValue.API_KEY, pageNumber = page)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _newsArticleld.value = it.articles
                    }
                    Log.i("Success", newsArticleld.value?.size.toString())
                } else {
                    Log.i("ERROR", "Response Error: ${response.code()}")
                }
            } catch (e: HttpException) {
                Log.i("ERROR", "HttpException: ${e.code()}")
            } catch (e: Exception) {
                Log.i("ERROR", "General Error: ${e.message}")
            }
            _isLoading.value = false
        }
    }

    fun findNews(query : String){
        viewModelScope.launch{
            _isLoading.value = true
            val response = newsApi.searchForNews(apiKey = ConstValue.API_KEY, searchQuery = query)
            try{
                if(response.isSuccessful){
                    response.body()?.let {
                        _findnewsArticleld.value = it.articles
                    }
                }else {
                    Log.i("ERROR", "Response Error: ${response.code()}")
                }
            }catch (e: HttpException) {
                Log.i("ERROR", "HttpException: ${e.code()}")
            } catch (e: Exception ) {
                Log.i("ERROR", "General Error: ${e.message}")
            }
            _isLoading.value = false
        }
    }



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