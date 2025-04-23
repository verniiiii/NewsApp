package com.example.newsapp.viewmodels

import NewsApi
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.MainDb
import com.example.newsapp.models.Article
import com.example.newsapp.models.ConstValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.net.UnknownHostException


class TopNewsViewModel(
    private val newsApi : NewsApi,

): ViewModel(){

    private val _newsArticleld = MutableStateFlow<List<Article>>(emptyList())
    val newsArticleld : StateFlow<List<Article>> = _newsArticleld

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading : MutableStateFlow<Boolean> = _isLoading

    private val _findnewsArticleld = MutableStateFlow<List<Article>>(emptyList())
    val findnewsArticleld : StateFlow<List<Article>> = _findnewsArticleld

    private val _error = MutableStateFlow<Boolean>(false)
    val error : StateFlow<Boolean> = _error

    private val _noresult = MutableStateFlow<Boolean>(false)
    val noresult : StateFlow<Boolean> = _noresult


    fun fetchNews(page : Int = 1,category: String = "") {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = newsApi.getNews(apiKey = ConstValue.API_KEY, pageNumber = page, category = category)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _newsArticleld.value = it.articles
                    }
                    _error.value = false
                    Log.i("Success", newsArticleld.value?.size.toString())
                } else {
                    _error.value = true
                    Log.i("ERROR", "Response Error: ${response.code()}")
                }
            } catch (e: HttpException) {
                _error.value = true
                Log.i("ERROR", "HttpException: ${e.code()}")
            } catch (e: Exception) {
                _error.value = true
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
                    _error.value = false
                    if (_findnewsArticleld.value.size == 0) {
                        _noresult.value = true
                    }else _noresult.value = false
                }else {
                    _error.value = true
                    Log.i("ERROR", "Response Error: ${response.code()}")
                    return@launch
                }
            }catch (e: HttpException) {
                _error.value = true
                Log.i("ERROR", "HttpException: ${e.code()}")
                return@launch
            } catch (e: Exception ) {
                _error.value = true
                Log.i("ERROR", "General Error: ${e.message}")
                return@launch
            }
            catch (e: UnknownHostException) {  // Ошибка сети
                _error.value = true
                Log.e("ERROR", "Нет подключения к интернету: ${e.message}")
                return@launch
            }
        }
        _isLoading.value = false
    }
}