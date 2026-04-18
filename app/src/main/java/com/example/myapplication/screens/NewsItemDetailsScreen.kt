package com.example.myapplication.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.model.Article

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsItemDetailsScreen(
    article: Article,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val canOpenFullStory = article.url.isNotBlank()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = article.source.name) },
                navigationIcon = {
                    IconButton(onClick =
                        onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (canOpenFullStory) {
                        IconButton(onClick = { openArticle(context, article.url) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                                contentDescription = "Open full story"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (article.imageUrl.isNotBlank()) {
                item {
                    AsyncImage(
                        model = article.imageUrl,
                        contentDescription = article.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
            }

            item {
                Text(
                    text = article.category,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            item {
                Text(
                    text = "${article.source.name} • ${article.timeAgo}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (article.description.isNotBlank()) {
                item {
                    Text(
                        text = article.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Placeholder for additional content (if we enrich Article later)
            item {
                Text(
                    text = "Stay informed. Content provided by ${article.source.name}.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (canOpenFullStory) {
                item {
                    Button(
                        onClick = { openArticle(context, article.url) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.height(0.dp))
                        Text(text = "Read full story")
                    }
                }
            }
        }
    }
}

private fun openArticle(context: android.content.Context, url: String) {
    val uri = Uri.parse(url)
    val customTabsIntent = CustomTabsIntent.Builder().build()
    try {
        customTabsIntent.launchUrl(context, uri)
    } catch (e: ActivityNotFoundException) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (ex: Exception) {
            Toast.makeText(context, "Unable to open article", Toast.LENGTH_SHORT).show()
        }
    }
}
