package com.example.newsapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.newsapp.models.Article
import com.example.newsapp.models.ConstValue
import com.example.newsapp.models.NewsRespons
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException


class topNewsViewModel(
    private val newsApi : NewsApi
): ViewModel(){

    private val _newsArticleld = MutableStateFlow<List<Article>>(emptyList())
    val newsArticleld : StateFlow<List<Article>> = _newsArticleld

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading : MutableStateFlow<Boolean> = _isLoading

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
}