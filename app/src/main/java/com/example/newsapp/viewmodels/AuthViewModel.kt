package com.example.newsapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.User
import com.example.newsapp.data.AppDatabase
import kotlinx.coroutines.launch



class AuthViewModel(context: Context) : ViewModel() {

    private val db = AppDatabase.getDatabase(context)

    fun loginUser(login: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = db.userDao().getUserByLogin(login)
            if (user != null && user.password == password) {
                onResult(true) // Пользователь найден и пароль правильный
            } else {
                onResult(false) // Неверный логин или пароль
            }
        }
    }

    fun registerUser(login: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val existingUser = db.userDao().getUserByLogin(login)
            if (existingUser == null) {
                val newUser = User(login = login, password = password)
                val result = db.userDao().insertUser(newUser)
                onResult(result != -1L) // Если результат != -1, значит пользователь был добавлен
            } else {
                onResult(false) // Пользователь с таким логином уже существует
            }
        }
    }
}

