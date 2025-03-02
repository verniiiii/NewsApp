package com.example.newsapp.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import com.example.newsapp.viewmodels.AuthViewModel


@Composable
fun LoginScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = remember { AuthViewModel(context) }

    var loginText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = loginText,
            onValueChange = { loginText = it },
            label = { Text("Login") },
            maxLines = 1,
            modifier = Modifier.size(256.dp, 61.dp),
        )
        OutlinedTextField(
            value = passwordText,
            onValueChange = { passwordText = it },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Password") },
            maxLines = 1,
            modifier = Modifier.size(256.dp, 61.dp),
        )
        Row(
            Modifier.padding(top = 14.dp)
        ) {
            Button(
                modifier = Modifier.size(143.dp, 51.dp),
                shape = RectangleShape,
                onClick = {
                    navController.navigate("register_screen") // Переход к экрану регистрации
                }
            ) {
                Text("To Register")
            }
            Spacer(Modifier.size(18.dp))
            Button(
                modifier = Modifier.size(143.dp, 51.dp),
                shape = RectangleShape,
                onClick = {
                    // Логика для авторизации
                    authViewModel.loginUser(loginText, passwordText) { success ->
                        if (success) {
                            navController.navigate("top_news_screen") // Переход на экран новостей
                        } else {
                            Toast.makeText(context, "Invalid login or password", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            ) {
                Text("Login")
            }
        }
    }
}
