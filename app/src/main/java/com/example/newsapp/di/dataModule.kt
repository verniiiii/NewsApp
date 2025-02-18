package com.example.newsapp.di

import com.example.newsapp.models.NewsRespons
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val RetrofitModule = module{

    single{
        Retrofit.Builder()
            .baseUrl("https://newsapi.org/") // Указываем базовый URL для API
            .addConverterFactory(GsonConverterFactory.create()) // Указываем конвертер JSON в объект
            .build()
    }

    single{
        get<Retrofit>().create(NewsApi::class.java)
    }

}