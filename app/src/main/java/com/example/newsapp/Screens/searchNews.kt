package com.example.newsapp.Screens

import android.util.Log
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
import androidx.compose.material3.TextField
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

@Composable
fun searchNews(
    viewModel: topNewsViewModel = koinViewModel()
){
        val newArticle = viewModel.findnewsArticleld.collectAsState()
        val isLoading = viewModel.isLoading.collectAsState()
        var text by rememberSaveable { mutableStateOf("") }

        if (isLoading.value) {
            Column(
                Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
            }
        }else{
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = {
                        Text("Поиск")
                    },
                    maxLines = 1,
                    modifier = Modifier
                        .size(256.dp, 61.dp),
                )
                Spacer(modifier = Modifier.size(10.dp))
                Button(
                    onClick = { viewModel.findNews(text) }
                ) {
                    Text("Поиск")
                }
            }
            Spacer(modifier = Modifier.size(15.dp))
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