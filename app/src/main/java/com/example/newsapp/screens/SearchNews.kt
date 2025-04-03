package com.example.newsapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@Composable
fun SearchNews(
    navController: NavController,
    viewModel: TopNewsViewModel = koinViewModel(),
    searchHistoryManager: SearchHistoryManager = koinInject()
) {
    val newArticle = viewModel.findnewsArticleld.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val isError = viewModel.error.collectAsState()
    val isNoresult = viewModel.noresult.collectAsState()
    var text by rememberSaveable { mutableStateOf("") }

    val searchHistory = searchHistoryManager.getSearchHistory()

    // Фокус на поле ввода
    val focusManager = LocalFocusManager.current
    var isSearchFocused by rememberSaveable { mutableStateOf(false) }

    var lastChangeTime by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(text) {
        // Когда текст изменяется, сбрасываем время последнего изменения
        lastChangeTime = System.currentTimeMillis()
    }

    // Используем LaunchedEffect для выполнения автоматического поиска через 2 секунды
    LaunchedEffect(lastChangeTime) {
        delay(2000) // Ждём 2 секунды
        if (System.currentTimeMillis() - lastChangeTime >= 2000 && text.isNotEmpty()) {
            // Если прошло 2 секунды без изменений, выполняем поиск
            searchHistoryManager.addSearchQuery(text)
            viewModel.findNews(text)
            focusManager.clearFocus() // Убираем фокус с поля ввода
        }
    }

    if (isLoading.value) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            //.padding(top = 80.dp),
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
            //.padding(top = 60.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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
                        .onFocusChanged { focusState ->
                            // Отслеживаем фокус на поле
                            isSearchFocused = focusState.isFocused
                        },

                    trailingIcon = {
                        if (text.isNotEmpty()) {
                            IconButton(onClick = { text = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Очистить"
                                )
                            }
                        }
                    }

                )
                Spacer(modifier = Modifier.size(10.dp))
                Button(onClick = {
                    searchHistoryManager.addSearchQuery(text)
                    viewModel.findNews(text)
                    focusManager.clearFocus()
                }) {
                    Text("Поиск")
                }
            }
            Spacer(modifier = Modifier.size(5.dp))

            // История поиска
            if (isSearchFocused && searchHistory.isNotEmpty()) {
                LazyColumn {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("История поиска:", modifier = Modifier.padding(16.dp))
                            Text(
                                "Очистить",
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable {
                                        searchHistoryManager.clearSearchHistory()
                                        focusManager.clearFocus()
                                    }
                            )
                        }
                    }
                    item {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp) // Равномерное расстояние между элементами
                        ) {
                            items(searchHistory) { query ->
                                Box(
                                    modifier = Modifier
                                        .clickable {
                                            // Добавляем выбранный элемент в текст для поиска
                                            text = query
                                            // Можно повторно выполнить поиск
                                            viewModel.findNews(query)
                                            focusManager.clearFocus()
                                        }
                                        .padding(8.dp)
                                ) {
                                    Text(query)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.size(5.dp))
            }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (isError.value) {
                        item {
                            Text("Ошибка выполнения запроса")
                            Spacer(modifier = Modifier.size(5.dp))
                            Button(
                                onClick = {
                                    viewModel.findNews(text)
                                }
                            ) {
                                Text("Обновить")
                            }
                        }
                    }
                    if (isNoresult.value) {
                        item {
                            Text("Ничего не найдено")
                        }
                    } else {
                        items(newArticle.value) { article ->
                            newsCard(article, onNoteClick = {}, navController = navController)
                        }
                    }
                }
            }
        }
    }
