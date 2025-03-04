package com.example.newsapp.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
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
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel = koinViewModel()) {
    val context = LocalContext.current

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var registerError by remember { mutableStateOf("") } // Для отображения ошибки регистрации

    fun validateInputs(): Boolean {
        var valid = true

        // Валидация логина (почта)
        if (!Patterns.EMAIL_ADDRESS.matcher(login).matches()) {
            loginError = "Неверный формат email"
            valid = false
        } else {
            loginError = ""
        }

        // Валидация пароля
        if (password.length < 4) {
            passwordError = "Пароль должен быть минимум 4 символа"
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
                .padding(bottom = 8.dp)
        )
        if (loginError.isNotEmpty()) {
            Text(text = loginError, color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        if (passwordError.isNotEmpty()) {
            Text(text = passwordError, color = MaterialTheme.colorScheme.error)
        }

        // Показываем ошибку регистрации
        if (registerError.isNotEmpty()) {
            Text(
                text = registerError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                if (validateInputs()) {
                    authViewModel.registerUser(login, password) { result ->
                        result.onSuccess {
                            navController.navigate("login_screen")
                        }.onFailure { error ->
                            registerError = error.message ?: "Ошибка регистрации"
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зарегистрироваться")
        }
    }
}
