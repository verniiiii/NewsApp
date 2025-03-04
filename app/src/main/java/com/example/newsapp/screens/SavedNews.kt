package com.example.newsapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newsapp.viewmodels.TopNewsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SavedNewsScr(
    viewModel: TopNewsViewModel = koinViewModel(),
) {

    val savedArticles by viewModel.getAllArticle().collectAsState(initial = emptyList())

    LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 100.dp, bottom = 100.dp)) {
        items(savedArticles){
            article ->
            newsCard(article,
                onClick = {},
                onMoreClick = {
                    viewModel.deleteArticle(article.url)
                }
            )
        }
    }
}
