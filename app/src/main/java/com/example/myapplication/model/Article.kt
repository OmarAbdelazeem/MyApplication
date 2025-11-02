package com.example.myapplication.model

data class Article(
    val id: String,
    val title: String,
    val category: String,
    val source: NewsSource,
    val imageUrl: String,
    val timeAgo: String,
    val description: String = ""
)

data class NewsSource(
    val id: String,
    val name: String,
    val logoUrl: String = ""
)

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

