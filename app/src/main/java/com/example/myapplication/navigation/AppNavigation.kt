package com.example.myapplication.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.model.Article
import com.example.myapplication.screens.BookmarkScreen
import com.example.myapplication.screens.ExploreScreen
import com.example.myapplication.screens.HomeScreen
import com.example.myapplication.screens.NewsItemDetailsScreen
import com.example.myapplication.screens.SearchScreen
import com.example.myapplication.screens.TrendingScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.util.AppLogger

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: BottomNavItem.Home.route

    val articleKey = "article"
    val tag = "AppNavigation"
    
    // Hide bottom navigation on detail screens
    val showBottomBar = currentRoute !in listOf("trending", "search", "details")
    
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onItemClick = { route ->
                        if (currentRoute != route) {
                            navController.navigate(route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a deep stack of destinations
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                MyApplicationTheme {
                    HomeScreen(
                        onNavigateToTrending = { navController.navigate("trending") },
                        onNavigateToSearch = { navController.navigate("search") },
                        onArticleClick = { article ->
                            AppLogger.d(tag, "Navigate Home -> details (article=${article.id})")
                            navController.currentBackStackEntry?.savedStateHandle?.set(articleKey, article)
                            navController.navigate("details")
                        }
                    )
                }
            }
            composable(BottomNavItem.Explore.route) {
                MyApplicationTheme {
                    ExploreScreen(
                        onNavigateToAllTopics = { /* Handle navigation to all topics */ }
                    )
                }
            }
            composable(BottomNavItem.Bookmark.route) {
                MyApplicationTheme {
                    BookmarkScreen(
                        onArticleClick = { article ->
                            AppLogger.d(tag, "Navigate Bookmark -> details (article=${article.id})")
                            navController.currentBackStackEntry?.savedStateHandle?.set(articleKey, article)
                            navController.navigate("details")
                        },
                        onNavigateToSearch = { navController.navigate("search") }
                    )
                }
            }
//            composable(BottomNavItem.Profile.route) {
//                MyApplicationTheme {
//                    ProfileScreen(
//                        onSettingsClick = { /* Handle settings */ },
//                        onEditProfileClick = { /* Handle edit profile */ },
//                        onWebsiteClick = { /* Handle website */ },
//                        onFabClick = { /* Handle FAB click */ }
//                    )
//                }
//            }
            composable("trending") {
                MyApplicationTheme {
                    TrendingScreen(
                        onBackClick = { navController.popBackStack() },
                        onArticleClick = { article ->
                            AppLogger.d(tag, "Navigate Trending -> details (article=${article.id})")
                            navController.currentBackStackEntry?.savedStateHandle?.set(articleKey, article)
                            navController.navigate("details")
                        }
                    )
                }
            }
            composable("search") {
                MyApplicationTheme {
                    SearchScreen(
                        onBackClick = { navController.popBackStack() },
                        onArticleClick = { article ->
                            AppLogger.d(tag, "Navigate Search -> details (article=${article.id})")
                            navController.currentBackStackEntry?.savedStateHandle?.set(articleKey, article)
                            navController.navigate("details")
                        }
                    )
                }
            }
            composable("details") {
                                val article: Article? = navController.previousBackStackEntry?.savedStateHandle?.get(articleKey)
        if(article!=null){
            MyApplicationTheme {
                NewsItemDetailsScreen(
                    article = article,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }

            }
        }
    }
}

