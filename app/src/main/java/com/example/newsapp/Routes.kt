package com.example.newsapp

import androidx.compose.runtime.MutableState
import kotlinx.serialization.Serializable

@Serializable
object loginScreen

@Serializable
object topNewsScreen

@Serializable
object searchNews

@Serializable
object registerScreen

@Serializable
object  savedNews

@Serializable
data class webView(val url: String)