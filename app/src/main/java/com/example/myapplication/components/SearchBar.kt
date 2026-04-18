package com.example.myapplication.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(

    onNavigateToSearch: () -> Unit,

    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onNavigateToSearch() },
        shape = RoundedCornerShape(12.dp),
        color = OutlinedTextFieldDefaults.colors().unfocusedContainerColor,
        shadowElevation = 0.dp,
        tonalElevation = 0.dp,
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = OutlinedTextFieldDefaults.colors().unfocusedLeadingIconColor
            )
            Text(
                text = "Search",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                color = OutlinedTextFieldDefaults.colors().unfocusedTextColor
            )
            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = OutlinedTextFieldDefaults.colors().unfocusedTrailingIconColor
                )
            }
        }
    }
}
