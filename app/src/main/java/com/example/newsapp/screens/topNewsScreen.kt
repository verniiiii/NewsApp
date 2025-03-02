package com.example.newsapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newsapp.viewmodels.topNewsViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun topNewsScreen(
    navController: NavController,
    viewModel: topNewsViewModel = koinViewModel()
){
    val newArticle = viewModel.newsArticleld.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()

    var pages = rememberSaveable { mutableStateOf(1) }

    LaunchedEffect(Unit) {
        Log.i("FETCH","START FIND")
        viewModel.fetchNews()
    }

    if (isLoading.value) {
       Column(
           Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
           verticalArrangement = Arrangement.Center,
           horizontalAlignment = Alignment.CenterHorizontally
       ) {
           CircularProgressIndicator(modifier = Modifier.size(50.dp))
       }
    }else{
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 75.dp)
            .background(MaterialTheme.colorScheme.background)
    )
        {
            items(newArticle.value){ article ->
                newsCard(article,
                    onClick = {},
                    onMoreClick = {}
                )
            }
        }
    }
}

