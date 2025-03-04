package com.example.newsapp.di

import NewsApi
import android.content.Context
import androidx.room.Room
import com.example.newsapp.Conv
import com.example.newsapp.data.MainDb
import com.example.newsapp.models.NewsRespons
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.sin


fun createDataBase(context: Context): MainDb {
    return Room.databaseBuilder(
        context,
        MainDb::class.java,
        "Articles.db"
    ).addTypeConverter(Conv())
        .fallbackToDestructiveMigration()
        .build()
}



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


val RoomArticleModule = module{

    single{
        createDataBase(get())
    }


}