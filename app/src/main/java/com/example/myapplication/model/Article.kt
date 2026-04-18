package com.example.myapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val id: String,
    val url: String = "",
    val title: String,
    val category: String,
    val source: NewsSource,
    val imageUrl: String,
    val timeAgo: String,
    val description: String = ""
): Parcelable

@Parcelize
data class NewsSource(
    val id: String,
    val name: String,
    val logoUrl: String = ""
): Parcelable

enum class Category(val displayName: String) {
    ALL("All"),
    SPORTS("Sports"),
    POLITICS("Politics"),
    BUSINESS("Business"),
    HEALTH("Health"),
    TRAVEL("Travel"),
    SCIENCE("Science"),
    EUROPE("Europe")
}

