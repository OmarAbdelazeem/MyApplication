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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.components.AppHeader
import com.example.myapplication.components.ArticleCard
import com.example.myapplication.components.CategoryTabs
import com.example.myapplication.components.SearchBar
import com.example.myapplication.components.TrendingCard
import com.example.myapplication.model.Article
import com.example.myapplication.model.Category
import com.example.myapplication.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onArticleClick: (Article) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val trendingArticle by viewModel.trendingArticle.collectAsState()
    val latestArticles by viewModel.latestArticles.collectAsState()
    
    val categories = listOf(
        Category.ALL,
        Category.SPORTS,
        Category.POLITICS,
        Category.BUSINESS,
        Category.HEALTH,
        Category.TRAVEL,
        Category.SCIENCE
    )
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // App Header
        AppHeader()
        
        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.updateSearchQuery(it) }
        )
        
        // Content
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Trending Section
            item {
                SectionHeader(
                    title = "Trending",
                    onSeeAllClick = { /* Handle see all */ }
                )
            }
            
            item {
                trendingArticle?.let { article ->
                    TrendingCard(
                        article = article,
                        onArticleClick = onArticleClick
                    )
                }
            }
            
            // Latest Section
            item {
                SectionHeader(
                    title = "Latest",
                    onSeeAllClick = { /* Handle see all */ }
                )
            }
            
            // Category Tabs
            item {
                CategoryTabs(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { viewModel.selectCategory(it) }
                )
            }
            
            // Articles List
            items(
                items = latestArticles,
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
fun SectionHeader(
    title: String,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        TextButton(
            onClick = onSeeAllClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Text(
                text = "See all",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

