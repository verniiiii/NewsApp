package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.screens.LoginScreen
import com.example.newsapp.screens.RegisterScreen
import com.example.newsapp.screens.searchNews
import com.example.newsapp.screens.topNewsScreen
import com.example.newsapp.ui.theme.NewsAppTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkThemeState = false
            NewsAppTheme(darkTheme = darkThemeState) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login_screen") {
                    composable("login_screen") {
                        LoginScreen(navController)
                    }
                    composable("top_news_screen") {
                        topNewsScreen(navController)
                    }
                    composable("search_news") {
                        searchNews()
                    }
                    composable("register_screen") {
                        RegisterScreen(navController)
                    }
                }
            }
        }
    }
}
