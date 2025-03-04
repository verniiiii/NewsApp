package com.example.newsapp.di

import android.content.Context
import com.example.newsapp.data.AppDatabase
import com.example.newsapp.data.UserDao
import org.koin.dsl.module

val databaseModule = module {
    single { provideDatabase(get()) }
    single { provideUserDao(get()) }
}

fun provideDatabase(context: Context): AppDatabase {
    return AppDatabase.getDatabase(context)
}

fun provideUserDao(appDatabase: AppDatabase): UserDao {
    return appDatabase.userDao()
}
