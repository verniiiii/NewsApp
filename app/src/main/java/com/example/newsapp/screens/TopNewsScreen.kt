package com.example.newsapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newsapp.loginScreen
import com.example.newsapp.savedNews
import com.example.newsapp.searchNews
import com.example.newsapp.topNewsScreen
import com.example.newsapp.viewmodels.TopNewsViewModel
import com.example.newsapp.webView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNewsScreen(
    navController: NavController,
    viewModel: TopNewsViewModel = koinViewModel(),
    userPreferences: UserPreferences = koinInject()
) {
    val newArticle = viewModel.newsArticleld.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    var pages = rememberSaveable { mutableStateOf(1) }

    // Асинхронный запрос на загрузку новостей
    LaunchedEffect(Unit) {
        Log.i("FETCH", "START FIND")
        viewModel.fetchNews()
    }
        if (isLoading.value) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                    //.padding(top = 85.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    //.padding(top = 85.dp)
            ) {
                items(newArticle.value) { article ->
                    newsCard(
                        article,
                        onNoteClick = {},
                        navController = navController
                    )
                }
            }
    }
}
