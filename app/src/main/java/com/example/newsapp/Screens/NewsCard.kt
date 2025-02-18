package com.example.newsapp.Screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.newsapp.R
import com.example.newsapp.models.Article
import com.example.newsapp.models.Source

@Composable
fun newsCard(
    article: Article,
    onClick: () -> Unit,
    onMoreClick: () -> Unit,
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.padding(10.dp),
        onClick = { onClick() }
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

                // Кнопка для доп.действий
                IconButton(onClick = { onMoreClick() }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Actions",
                        tint = Color.Blue
                    )
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
        urlToImage = "https://via.placeholder.com/150"
    )

    newsCard(article = article, onClick = { /* handle click */ }, onMoreClick = { /* handle save */ })
}