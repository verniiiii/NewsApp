package com.example.newsapp.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.newsapp.data.AppDatabase
import com.example.newsapp.viewmodels.AuthViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp


import android.util.Patterns

@Composable
fun RegisterScreen(navController: NavController) {
    // Получаем контекст и создаем AuthViewModel
    val context = LocalContext.current
    val authViewModel: AuthViewModel = remember { AuthViewModel(context) }

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    // Валидация логина и пароля
    fun validateInputs(): Boolean {
        var valid = true

        // Валидация логина (почта)
        if (!Patterns.EMAIL_ADDRESS.matcher(login).matches()) {
            loginError = "Invalid email format"
            valid = false
        } else {
            loginError = ""
        }

        // Валидация пароля
        if (password.length < 4) {
            passwordError = "Password must be at least 4 characters"
            valid = false
        } else {
            passwordError = ""
        }

        return valid
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Email") },
            isError = loginError.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        if (loginError.isNotEmpty()) {
            Text(text = loginError, color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        if (passwordError.isNotEmpty()) {
            Text(text = passwordError, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                if (validateInputs()) {
                    authViewModel.registerUser(login, password) { success ->
                        if (success) {
                            navController.navigate("login_screen") // Переход на экран логина
                        } else {
                            Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}
