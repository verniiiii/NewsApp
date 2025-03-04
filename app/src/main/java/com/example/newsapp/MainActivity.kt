package com.example.newsapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.newsapp.data.UserPreferences
import com.example.newsapp.screens.LoginScreen
import com.example.newsapp.screens.RegisterScreen
import com.example.newsapp.screens.SavedNewsScr
import com.example.newsapp.screens.SearchNews
import com.example.newsapp.screens.TopNewsScreen
import com.example.newsapp.screens.webViewScreen
import com.example.newsapp.ui.theme.NewsAppTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPreferences = UserPreferences(this)

        lifecycleScope.launch {
            val userId: Int? = userPreferences.userId.first()
            val startDestination = if (userId != null) topNewsScreen else loginScreen

            setContent {
                val navController = rememberNavController()
                val currentBackStackEntry = navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry.value?.destination?.route
                NewsAppTheme(
                    darkTheme = false
                ) {
                    Scaffold(
                        topBar = {
                            if (currentRoute !in listOf(
                                    loginScreen::class.qualifiedName,
                                    registerScreen::class.qualifiedName,
                                )) {
                                TopBar(navController)
                            }
                        }
                    ) {
                        NavHost(navController = navController, startDestination = startDestination) {
                            composable<loginScreen>{
                                LoginScreen(navController)
                            }
                            composable<topNewsScreen> {
                                TopNewsScreen(navController)
                            }
                            composable<searchNews> {
                                SearchNews(navController)
                            }
                            composable<registerScreen> {
                                RegisterScreen(navController)
                            }
                            composable<savedNews>{
                                SavedNewsScr(navController)
                            }
                            composable<webView>{ backStakEntry ->
                                val url  = backStakEntry.toRoute<webView>()
                                webViewScreen(url.url)
                            }
                        }
                    }
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    userPreferences: UserPreferences = koinInject()
){
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = {
            when(currentRoute){
                topNewsScreen::class.qualifiedName -> Text("Популярные новости")
                searchNews::class.qualifiedName -> Text("Поиск новостей")
                savedNews::class.qualifiedName -> Text("Сохранённые новости")
                else -> Text("Новость")
            }
        },
        actions = {
            IconButton(onClick = {
                scope.launch {
                    userPreferences.clearUserId()
                    navController.navigate(loginScreen) {
                        popUpTo(topNewsScreen) { inclusive = true }
                    }
                }
            }) {
                Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Выход")
            }
            IconButton(onClick = {
                navController.navigate(searchNews)
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Поиск")
            }
            IconButton(onClick = {
                navController.navigate(savedNews)
            }) {
                Icon(imageVector = Icons.Default.Favorite, contentDescription = "Сохр")
            }
        }
    )
}