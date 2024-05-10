package com.example.docscanner.feature_createpdf.presentation.createpdf.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyzerScreenToolBar(
    modifier: Modifier = Modifier,
    onNotificationClicked: () -> Unit,
    scrollBehavior : TopAppBarScrollBehavior,
    onNavigationDrawerClicked : () -> Unit
){
    Box(
        modifier = modifier
    ) {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                scrolledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                titleContentColor = MaterialTheme.colorScheme.surface
            ),
            title = {
                Text(
                    text = "ScanSage",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { onNavigationDrawerClicked() },
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "navigation drawer",
                        tint = Color.White
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = { onNotificationClicked() },
                    colors = IconButtonDefaults.iconButtonColors(
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "notification",
                        tint = Color.White
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )
    }
}
