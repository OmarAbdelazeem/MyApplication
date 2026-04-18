package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.remote.NewsApiClient
import com.example.myapplication.data.repository.NewsRepository
import com.example.myapplication.model.Article
import com.example.myapplication.model.Category
import com.example.myapplication.util.AppLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isInitialLoading: Boolean = true,
    val trendingArticles: List<Article> = emptyList(),
    val latestArticles: List<Article> = emptyList(),
    val selectedCategory: Category = Category.ALL,
    val categoryArticles: List<Article> = emptyList(),
    val isCategoryLoading: Boolean = false,
    val errorMessage: String? = null,
    val categoryError: String? = null
) {
    val trendingArticle: Article? get() = trendingArticles.firstOrNull()
}

class HomeViewModel(
    private val repository: NewsRepository = NewsRepository(NewsApiClient.createService())
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val categoryCache = mutableMapOf<Category, List<Article>>()

    private val trendingRequest = CategoryRequest(categoryParam = "general")

    init {
        refreshHome()
    }

    fun refreshHome() {
        viewModelScope.launch {
            _uiState.update { it.copy(isInitialLoading = true, errorMessage = null) }
            try {
                AppLogger.d(TAG, "Refreshing home feed")
                val trending = repository.getTrending(
                    pageSize = 6,
                    category = trendingRequest.categoryParam,
                    query = trendingRequest.query
                )
                val latest = repository.getLatest(pageSize = 20)
                AppLogger.d(TAG, "Trending loaded: ${trending.size} items, latest: ${latest.size} items")

                categoryCache[Category.ALL] = latest

                _uiState.update {
                    it.copy(
                        isInitialLoading = false,
                        trendingArticles = trending,
                        latestArticles = latest,
                        selectedCategory = Category.ALL,
                        categoryArticles = latest,
                        errorMessage = null,
                        categoryError = null,
                        isCategoryLoading = false
                    )
                }
            } catch (e: Exception) {
                AppLogger.e(TAG, "Failed to refresh home", e)
                _uiState.update {
                    it.copy(
                        isInitialLoading = false,
                        errorMessage = e.message ?: "Failed to load news"
                    )
                }
            }
        }
    }

    fun selectCategory(category: Category) {
        val current = _uiState.value.selectedCategory
        if (category != current) {
            _uiState.update { it.copy(selectedCategory = category, categoryError = null) }
        }

        if (category == Category.ALL) {
            val articles = categoryCache[Category.ALL].orEmpty()
            _uiState.update { it.copy(categoryArticles = articles, isCategoryLoading = false, categoryError = null) }
            return
        }

        if (categoryCache.containsKey(category) && category == current) {
            _uiState.update { it.copy(categoryArticles = categoryCache[category].orEmpty(), isCategoryLoading = false) }
            return
        }

        if (!categoryCache.containsKey(category)) {
            loadCategory(category)
        } else {
            _uiState.update { it.copy(categoryArticles = categoryCache[category].orEmpty(), isCategoryLoading = false) }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun retrySelectedCategory() {
        val category = _uiState.value.selectedCategory
        if (category == Category.ALL) {
            val articles = categoryCache[Category.ALL].orEmpty()
            _uiState.update {
                it.copy(
                    categoryArticles = articles,
                    categoryError = null,
                    isCategoryLoading = false
                )
            }
        } else {
            loadCategory(category, forceRefresh = true)
        }
    }

    private fun loadCategory(category: Category, forceRefresh: Boolean = false) {
        val request = category.toRequest()
        if (!forceRefresh && categoryCache.containsKey(category)) {
            _uiState.update {
                it.copy(
                    categoryArticles = categoryCache[category].orEmpty(),
                    isCategoryLoading = false,
                    categoryError = null
                )
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isCategoryLoading = true, categoryError = null) }
            try {
                AppLogger.d(TAG, "Loading category ${category.name}")
                val articles = repository.getByCategory(
                    category = request.categoryParam,
                    query = request.query,
                    pageSize = 20
                )
                AppLogger.d(TAG, "Category ${category.name} loaded with ${articles.size} articles")
                categoryCache[category] = articles
                // Ensure currently selected category matches to avoid overwriting outdated selection
                _uiState.update {
                    if (it.selectedCategory == category) {
                        it.copy(
                            categoryArticles = articles,
                            isCategoryLoading = false,
                            categoryError = null
                        )
                    } else {
                        it.copy(isCategoryLoading = false)
                    }
                }
            } catch (e: Exception) {
                AppLogger.e(TAG, "Failed to load category ${category.name}", e)
                _uiState.update {
                    if (it.selectedCategory == category) {
                        it.copy(
                            isCategoryLoading = false,
                            categoryError = e.message ?: "Failed to load category"
                        )
                    } else {
                        it.copy(isCategoryLoading = false)
                    }
                }
            }
        }
    }

    private fun Category.toRequest(): CategoryRequest = when (this) {
        Category.ALL -> CategoryRequest()
        Category.SPORTS -> CategoryRequest(categoryParam = "sports")
        Category.POLITICS -> CategoryRequest(query = "politics")
        Category.BUSINESS -> CategoryRequest(categoryParam = "business")
        Category.HEALTH -> CategoryRequest(categoryParam = "health")
        Category.TRAVEL -> CategoryRequest(query = "travel")
        Category.SCIENCE -> CategoryRequest(categoryParam = "science")
        Category.EUROPE -> CategoryRequest(query = "europe")
    }
}

private data class CategoryRequest(
    val categoryParam: String? = null,
    val query: String? = null
)
