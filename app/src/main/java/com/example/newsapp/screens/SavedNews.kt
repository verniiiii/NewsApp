package com.example.newsapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newsapp.models.Article
import com.example.newsapp.viewmodels.CardViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue


@Composable
fun SavedNewsScr(
    navController: NavController,
    viewModel: CardViewModel = koinViewModel(),
) {

    val savedArticles by viewModel.getAllArticle().collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var selectedArticle by remember { mutableStateOf<Article?>(null) }
    var noteText by rememberSaveable { mutableStateOf("") }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 100.dp, bottom = 20.dp)) {
        items(savedArticles){
            article ->
            newsCard(article, navController = navController) {
                selectedArticle = article
                noteText = article.note ?: "" // Загружаем существующую заметку
                showDialog = true
            }
        }
    }
    if (showDialog && selectedArticle != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Редактирование заметки") },
            text = {
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Заметка") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    selectedArticle?.let {
                        viewModel.updateNote(it.url, noteText)
                    }
                    showDialog = false
                }) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}
