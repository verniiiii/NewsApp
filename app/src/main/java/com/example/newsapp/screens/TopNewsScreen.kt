package com.example.newsapp.screens

import UserPreferences
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
) {
    val newArticle = viewModel.newsArticleld.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    var pages = rememberSaveable { mutableStateOf(1) }
    val category = listOf<String>("sports","business","entertainment","general","health","science","technology")
    val checkedStates = rememberSaveable { BooleanArray(category.size) { false } }
    val childCheckedStates by rememberUpdatedState(checkedStates)
    val categoryNow = rememberSaveable { mutableStateOf("") }


    // Асинхронный запрос на загрузку новостей
    LaunchedEffect(Unit) {
        Log.i("FETCH", "START FIND")
        viewModel.fetchNews(pages.value,categoryNow.value)
    }
    LaunchedEffect(pages.value) {
        viewModel.fetchNews(pages.value,categoryNow.value)
    }
    LaunchedEffect(categoryNow.value) {
        viewModel.fetchNews(pages.value,categoryNow.value)
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
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
                    //.padding(top = 85.dp)
            ) {
                item{
                    LazyRow{
                        item{
                            childCheckedStates.forEachIndexed { index, checked ->
                                Row(
                                    modifier = Modifier.padding(start = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Checkbox(
                                        checked = checked,
                                        onCheckedChange = { isChecked ->
                                            if (isChecked) {
                                                //Сбрасываем все чекбоксы
                                                childCheckedStates.indices.forEach { i ->
                                                    childCheckedStates[i] = false
                                                }
                                                // Затем устанавливаем выбранный
                                                childCheckedStates[index] = true
                                                categoryNow.value = category[index]
                                            } else {
                                                //Обнуление категорий
                                                childCheckedStates[index] = false
                                                categoryNow.value = ""
                                            }
                                        }
                                    )
                                    Text(category[index])
                                }
                            }
                        }
                    }
                }
                items(newArticle.value) { article ->
                    newsCard(
                        article,
                        onNoteClick = {},
                        navController = navController
                    )
                }
                item {
                    Row {
                        Button(onClick = {
                            if(pages.value != 1) pages.value--
                        }) {
                            Text("Назад")
                        }
                        Spacer(modifier = Modifier.size(20.dp))
                        Button(onClick = {
                            pages.value++
                        }) {
                            Text("Далее")
                        }
                    }
                }
            }
    }
}
@Composable
@Preview
fun prevTop(){
    TopNewsScreen(navController = rememberNavController())
}