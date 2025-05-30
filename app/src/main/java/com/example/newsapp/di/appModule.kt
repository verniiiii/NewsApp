package com.example.newsapp.di


import SearchHistoryManager
import UserPreferences
import com.example.newsapp.data.AppDatabase
import com.example.newsapp.viewmodels.AuthViewModel
import com.example.newsapp.viewmodels.CardViewModel
import com.example.newsapp.viewmodels.TopNewsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module{
    viewModel{ TopNewsViewModel(get()) }
    viewModel{ CardViewModel(get(),get())}
    viewModel{ AuthViewModel(get<AppDatabase>(), get<UserPreferences>()) }
    single { UserPreferences(get())}
    single { SearchHistoryManager(get())}
}