package com.example.newsapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.newsapp.R
import com.example.newsapp.models.Article
import com.example.newsapp.models.Source
import com.example.newsapp.savedNews
import com.example.newsapp.viewmodels.CardViewModel
import com.example.newsapp.webView
import org.koin.androidx.compose.koinViewModel

@Composable
fun newsCard(
    article: Article,
    viewModel: CardViewModel = koinViewModel(),
    navController: NavController
) {

    val currentBackStack = navController.currentBackStackEntryAsState().value?.destination?.route
    Card(
        modifier = Modifier.padding(10.dp),
        onClick = {
            navController.navigate(webView(article.url))
        }
    ) {
        Column {
            // Показ изображения статьи
            if (article.urlToImage != null && article.urlToImage.isNotEmpty()) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    model = article.urlToImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.ic_launcher_background),
                    onError = { error ->
                        Log.e("AsyncImage", "Error loading image: ${error.result.throwable}")
                    }
                )
            }

            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text = article.title ?: "No Title", color = Color.Black)
                Spacer(modifier = Modifier.size(5.dp))
                Text(text = article.description ?: "", color = Color.Gray)


                IconButton(
                    onClick = {
                        if ( currentBackStack == savedNews::class.qualifiedName){
                            viewModel.deleteArticle(article.url)
                        }
                        else viewModel.addArticleToDb(article)
                }) {
                    Icon(
                        imageVector = if (currentBackStack == savedNews::class.qualifiedName ) Icons.Default.Delete
                        else Icons.Default.Add,
                        contentDescription = "Add news",
                        tint = Color.Blue
                    )
                }
                IconButton(onClick = {
                    //получаем сслыку
                }) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Share News",
                        tint = Color.Blue
                    )
                }
                if (currentBackStack == savedNews::class.qualifiedName){
                    IconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Note",
                            tint = Color.Blue
                        )
                    }
                }

            }
        }
    }
}

@Composable
@Preview
private fun CardShowPreview() {
    // Для примера добавим моковую статью
    val article = Article(
        author = "John Doe",
        content = "This is a sample content",
        description = "This is a sample description",
        publishedAt = "2024-11-06",
        title = "Sample Article Title",
        url = "https://example.com",
        source = Source(id = "1", name = "Example Source"),
        urlToImage = "https://via.placeholder.com/150",
        owner = 1
    )

    newsCard(article = article,  navController = rememberNavController())
}