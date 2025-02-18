package com.example.newsapp.di


import com.example.newsapp.viewmodels.topNewsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module{
    viewModel{ topNewsViewModel(get()) }
}