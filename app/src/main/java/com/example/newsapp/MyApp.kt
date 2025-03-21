package com.example.newsapp

import android.app.Application
import com.example.newsapp.di.RetrofitModule
import com.example.newsapp.di.RoomArticleModule
import com.example.newsapp.di.appModule
import com.example.newsapp.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@MyApp)
            modules(listOf(RetrofitModule, appModule, databaseModule, RoomArticleModule))
        }
    }
}