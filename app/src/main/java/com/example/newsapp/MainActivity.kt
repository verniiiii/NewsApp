package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.Screens.loginScreen
import com.example.newsapp.ui.theme.NewsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            val darkThemeState = isSystemInDarkTheme()
            NewsAppTheme(
                darkTheme = darkThemeState
            ) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = loginScreen){
                    composable<loginScreen>{
                        loginScreen()
                    }
                    }
                }
            }
    }
}
