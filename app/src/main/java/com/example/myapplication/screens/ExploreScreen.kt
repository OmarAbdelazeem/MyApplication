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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.components.SectionHeader
import com.example.myapplication.components.TopicCard
import com.example.myapplication.components.TrendingCard
import com.example.myapplication.model.Article
import com.example.myapplication.model.Topic
import com.example.myapplication.viewmodel.ExploreViewModel

@Composable
fun ExploreScreen(
    onArticleClick: (Article) -> Unit = {},
    onNavigateToAllTopics: () -> Unit = {},
    viewModel: ExploreViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val topics by viewModel.topics.collectAsState()
    val popularTopic by viewModel.popularTopic.collectAsState()
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Header
        Text(
            text = "Explore",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )
        
        // Content
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Topic Section
            item {
                SectionHeader(
                    title = "Topic",
                    onSeeAllClick = onNavigateToAllTopics
                )
            }
            
            items(
                items = topics,
                key = { it.id }
            ) { topic ->
                TopicCard(
                    topic = topic,
                    onSaveClick = { viewModel.toggleTopicSave(it) }
                )
            }
            
            // Popular Topic Section
            item {
                SectionHeader(
                    title = "Popular Topic",
                    onSeeAllClick = { /* Handle see all */ }
                )
            }
            
            item {
                popularTopic?.let { article ->
                    TrendingCard(
                        article = article,
                        onArticleClick = onArticleClick
                    )
                }
            }
        }
    }
}
