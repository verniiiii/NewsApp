package com.example.newsapp

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.newsapp.models.Source
import kotlin.let
import kotlin.text.split

@ProvidedTypeConverter
class Conv {

    @TypeConverter
    fun fromSource(source: Source): String {
        return "${source.id},${source.name}"
    }

    @TypeConverter
    fun toSource(sourceString: String): Source {
        return sourceString.split(",").let { sourceArray->
            Source(sourceArray[0], sourceArray[1])
        }
    }

}
