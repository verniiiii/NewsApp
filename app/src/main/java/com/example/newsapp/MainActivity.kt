package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.data.UserPreferences
import com.example.newsapp.screens.LoginScreen
import com.example.newsapp.screens.RegisterScreen
import com.example.newsapp.screens.SearchNews
import com.example.newsapp.screens.TopNewsScreen
import com.example.newsapp.ui.theme.NewsAppTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPreferences = UserPreferences(this)

        lifecycleScope.launch {
            // Получаем userId (если он есть, то это число, если нет - null)
            val userId:Int? = userPreferences.userId.first() // ✅ Проверяем сохранённого пользователя

            // Переопределяем начальный экран в зависимости от наличия userId
            val startDestination = if (userId != null) "Top_news_screen" else "login_screen"

            setContent {
                val darkThemeState = false
                NewsAppTheme(darkTheme = darkThemeState) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("login_screen") {
                            LoginScreen(navController)
                        }
                        composable("Top_news_screen") {
                            TopNewsScreen(navController)
                        }
                        composable("search_news") {
                            SearchNews()
                        }
                        composable("register_screen") {
                            RegisterScreen(navController)
                        }
                    }
                }
            }
        }
    }
}
