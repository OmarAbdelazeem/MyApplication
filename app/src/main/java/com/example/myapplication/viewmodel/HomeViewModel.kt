package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Article
import com.example.myapplication.model.Category
import com.example.myapplication.model.NewsSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    
    private val _selectedCategory = MutableStateFlow(Category.ALL)
    val selectedCategory: StateFlow<Category> = _selectedCategory.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _trendingArticle = MutableStateFlow<Article?>(null)
    val trendingArticle: StateFlow<Article?> = _trendingArticle.asStateFlow()
    
    private val _latestArticles = MutableStateFlow<List<Article>>(emptyList())
    val latestArticles: StateFlow<List<Article>> = _latestArticles.asStateFlow()
    
    init {
        loadMockData()
    }
    
    private fun loadMockData() {
        viewModelScope.launch {
            // Mock trending article
            _trendingArticle.value = Article(
                id = "1",
                title = "Russian warship: Moskva sinks in Black Sea",
                category = "Europe",
                source = NewsSource(id = "bbc", name = "BBC News", logoUrl = ""),
                imageUrl = "https://images.unsplash.com/photo-1544552866-e04334b918c8?w=800",
                timeAgo = "4h ago",
                description = "The flagship of Russia's Black Sea fleet has sunk, Moscow says."
            )
            
            // Mock latest articles
            _latestArticles.value = listOf(
                Article(
                    id = "2",
                    title = "Ukraine's President Zelensky to BBC: Blood money being paid...",
                    category = "Europe",
                    source = NewsSource(id = "bbc", name = "BBC News", logoUrl = ""),
                    imageUrl = "https://images.unsplash.com/photo-1576092768241-dec231879fc3?w=400",
                    timeAgo = "14m ago"
                ),
                Article(
                    id = "3",
                    title = "Her train broke down. Her phone died. And then she met her...",
                    category = "Travel",
                    source = NewsSource(id = "cnn", name = "CNN", logoUrl = ""),
                    imageUrl = "https://images.unsplash.com/photo-1511285560929-80b456fea0bc?w=400",
                    timeAgo = "2h ago"
                ),
                Article(
                    id = "4",
                    title = "New breakthrough in renewable energy research",
                    category = "Science",
                    source = NewsSource(id = "bbc", name = "BBC News", logoUrl = ""),
                    imageUrl = "https://images.unsplash.com/photo-1466611653911-95081537e5b7?w=400",
                    timeAgo = "5h ago"
                ),
                Article(
                    id = "5",
                    title = "Market reaches new all-time high",
                    category = "Business",
                    source = NewsSource(id = "cnn", name = "CNN", logoUrl = ""),
                    imageUrl = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=400",
                    timeAgo = "1h ago"
                )
            )
        }
    }
    
    fun selectCategory(category: Category) {
        _selectedCategory.value = category
        filterArticles()
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterArticles()
    }
    
    private fun filterArticles() {
        val category = _selectedCategory.value
        val query = _searchQuery.value.lowercase()
        
        // In a real app, this would filter from a larger dataset
        val filtered = _latestArticles.value.filter { article ->
            (category == Category.ALL || article.category == category.displayName) &&
            (query.isEmpty() || article.title.lowercase().contains(query))
        }
        
        // For demo, we keep showing all articles
        // In production, you'd update _latestArticles with filtered results
    }
}

