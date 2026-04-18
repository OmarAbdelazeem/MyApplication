package com.example.myapplication.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.Purple40

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object Explore : BottomNavItem("explore", "Explore", Icons.Default.Explore)
    object Bookmark : BottomNavItem("bookmark", "Bookmark", Icons.Default.Bookmark)
//    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Explore,
        BottomNavItem.Bookmark,
//        BottomNavItem.Profile
    )
    
    NavigationBar(
        modifier = modifier.height(64.dp),
        containerColor = Color.White
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = if (isSelected) Purple40 else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 12.sp,
                        color = if (isSelected) Purple40 else Color.Gray
                    )
                },
                selected = isSelected,
                onClick = { onItemClick(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Purple40,
                    selectedTextColor = Purple40,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

