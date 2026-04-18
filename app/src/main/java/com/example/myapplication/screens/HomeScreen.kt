package com.example.myapplication.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.components.AppHeader
import com.example.myapplication.components.ArticleCard
import com.example.myapplication.components.CategoryTabs
import com.example.myapplication.components.SearchBar
import com.example.myapplication.components.SectionHeader
import com.example.myapplication.components.TrendingCard
import com.example.myapplication.model.Article
import com.example.myapplication.model.Category
import com.example.myapplication.viewmodel.HomeViewModel
import com.example.myapplication.viewmodel.HomeUiState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onArticleClick: (Article) -> Unit = {},
    onNavigateToTrending: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    val categories = listOf(
        Category.ALL,
        Category.SPORTS,
        Category.POLITICS,
        Category.BUSINESS,
        Category.HEALTH,
        Category.TRAVEL,
        Category.SCIENCE
    )
    
    val errorMessage = uiState.errorMessage
    val trendingArticle = uiState.trendingArticle

    when {
        uiState.isInitialLoading -> HomeLoading()
        errorMessage != null -> HomeError(
            message = errorMessage,
            onRetry = { viewModel.refreshHome() }
        )
        else -> HomeContent(
            uiState = uiState,
            categories = categories,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToTrending = onNavigateToTrending,
            onArticleClick = onArticleClick,
            onSelectCategory = { viewModel.selectCategory(it) },
            onRetryCategory = { viewModel.retrySelectedCategory() },
            trendingArticle = trendingArticle
        )
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    categories: List<Category>,
    onNavigateToSearch: () -> Unit,
    onNavigateToTrending: () -> Unit,
    onArticleClick: (Article) -> Unit,
    onSelectCategory: (Category) -> Unit,
    onRetryCategory: () -> Unit,
    trendingArticle: Article?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        AppHeader()
        SearchBar(onNavigateToSearch = onNavigateToSearch)

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                SectionHeader(
                    title = "Trending",
                    onSeeAllClick = onNavigateToTrending
                )
            }

            item {
                when {
                    trendingArticle != null -> TrendingCard(
                        article = trendingArticle,
                        onArticleClick = onArticleClick
                    )

                    uiState.trendingArticles.isEmpty() -> Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No trending articles available")
                    }
                }
            }

            item {
                SectionHeader(
                    title = "Latest",
                    onSeeAllClick = { /* TODO: hook see all */ }
                )
            }

            item {
                CategoryTabs(
                    categories = categories,
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelected = onSelectCategory
                )
            }

            if (uiState.isCategoryLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            uiState.categoryError?.let { error ->
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = error)
                        Button(onClick = onRetryCategory) {
                            Text(text = "Retry")
                        }
                    }
                }
            }

            items(
                items = uiState.categoryArticles,
                key = { it.id }
            ) { article ->
                ArticleCard(
                    article = article,
                    onArticleClick = onArticleClick
                )
            }
        }
    }
}

@Composable
private fun HomeLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun HomeError(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = message)
            Button(onClick = onRetry) {
                Text(text = "Retry")
            }
        }
    }
}
