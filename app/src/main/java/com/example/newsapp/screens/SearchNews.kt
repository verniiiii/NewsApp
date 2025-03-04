package com.example.newsapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newsapp.viewmodels.TopNewsViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.navigation.NavController

@Composable
fun SearchNews(
    navController: NavController,
    viewModel: TopNewsViewModel = koinViewModel()
) {
    val newArticle = viewModel.findnewsArticleld.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    var text by rememberSaveable { mutableStateOf("") }

    if (isLoading.value) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 80.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
        }
    } else {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 60.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Поиск") },
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Button(onClick = { viewModel.findNews(text) }) {
                    Text("Поиск")
                }
            }
            Spacer(modifier = Modifier.size(5.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(newArticle.value) { article ->
                    newsCard(article, onNoteClick = {}, navController = navController)
                }
            }
        }
    }
}
