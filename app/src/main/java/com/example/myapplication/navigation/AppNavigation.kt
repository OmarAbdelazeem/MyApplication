package com.example.myapplication.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.screens.ExploreScreen
import com.example.myapplication.screens.HomeScreen
import com.example.myapplication.screens.PlaceholderScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: BottomNavItem.Home.route
    
    Scaffold(
        bottomBar = {
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                MyApplicationTheme {
                    HomeScreen()
                }
            }
            composable(BottomNavItem.Explore.route) {
                MyApplicationTheme {
                    ExploreScreen()
                }
            }
            composable(BottomNavItem.Bookmark.route) {
                MyApplicationTheme {
                    PlaceholderScreen(title = "Bookmark")
                }
            }
            composable(BottomNavItem.Profile.route) {
                MyApplicationTheme {
                    PlaceholderScreen(title = "Profile")
                }
            }
        }
    }
}

