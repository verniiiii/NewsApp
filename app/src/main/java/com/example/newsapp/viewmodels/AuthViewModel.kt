package com.example.newsapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.User
import com.example.newsapp.data.AppDatabase
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.security.MessageDigest

class AuthViewModel(private val db: AppDatabase, private val userPreferences: UserPreferences) : ViewModel() {


    fun loginUser(login: String, password: String, onResult: (Result<String>) -> Unit) {
        viewModelScope.launch {
            val user = db.userDao().getUserByLogin(login)
            if (user == null) {
                onResult(Result.failure(Exception("Пользователь не найден")))
            } else if (user.password != hashPassword(password)) {
                onResult(Result.failure(Exception("Неверный пароль")))
            } else {
                userPreferences.saveUserId(user.id)
                onResult(Result.success("Вход выполнен успешно"))
            }
        }
    }


    fun registerUser(login: String, password: String, onResult: (Result<String>) -> Unit) {
        viewModelScope.launch {
            try {
                val existingUser = db.userDao().getUserByLogin(login)
                if (existingUser != null) {
                    onResult(Result.failure(Exception("Пользователь с таким логином уже существует")))
                } else {
                    val hashedPassword = hashPassword(password)
                    val newUser = User(login = login, password = hashedPassword)
                    val result = db.userDao().insertUser(newUser)
                    if (result != -1L) {
                        onResult(Result.success("Регистрация успешна"))
                    } else {
                        onResult(Result.failure(Exception("Ошибка при регистрации пользователя")))
                    }
                }
            } catch (e: Exception) {
                onResult(Result.failure(Exception("Ошибка базы данных: ${e.message}")))
            }
        }
    }

    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}
