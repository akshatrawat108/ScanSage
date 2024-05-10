package com.abhinav.idanalyzer.feature_id_analyzer.presentation.analyzer_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.Logout
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeNavigationDrawer(
    modifier : Modifier = Modifier,
    drawerState: DrawerState,
    onWhatsNewClicked: () -> Unit,
    onLogOutClicked: () -> Unit,
    onTurnOffNotifications: (Boolean) -> Unit,
    isNotificationsEnabled: Boolean,
    content: @Composable () -> Unit,
){
    ModalNavigationDrawer(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.onPrimaryContainer),
        drawerContent = {
            NavigationDrawerSheet(
                modifier = Modifier.width(270.dp),
                onLogOutClicked = {
                    onLogOutClicked()
                },
                onWhatsNewClicked = {
                    onWhatsNewClicked()
                },
                onTurnOffNotifications = {
                    onTurnOffNotifications(it)
                },
                isNotificationsEnabled = isNotificationsEnabled,
            )
        },
        content = {content()},
        gesturesEnabled = true,
        drawerState = drawerState,
    )
}

@Composable
fun NavigationDrawerSheet(
    modifier: Modifier = Modifier,
    onLogOutClicked: () -> Unit,
    onWhatsNewClicked: () -> Unit,
    onTurnOffNotifications: (Boolean) -> Unit,
    isNotificationsEnabled: Boolean
){
    ModalDrawerSheet(
        modifier = modifier,
        drawerContentColor = MaterialTheme.colorScheme.surface,
        drawerContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
        drawerTonalElevation = 20.dp,
    ) {
        Icon(
            modifier = Modifier
                .size(80.dp)
                .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 5.dp)
                .clickable {
                },
            imageVector = Icons.Default.Sort,
            contentDescription = "App logo",
            tint = MaterialTheme.colorScheme.surface
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
            text = "ScanSage",
            fontSize = 18.sp,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.surface
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {

            TextIconLayout(
                text = "What's New",
                icon = Icons.Default.NewReleases,
                onClick = {onWhatsNewClicked()}
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart,
            ){
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    text = "Turn off Notifications",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.surface,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Start
                )

                var checked by rememberSaveable { mutableStateOf(isNotificationsEnabled) }

                Switch(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp),
                    checked = checked,
                    onCheckedChange = {it->
                        checked = it
                        onTurnOffNotifications(checked)
                    }
                )
            }

           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .clickable {
                       onLogOutClicked()
                   }
                   .padding(horizontal = 16.dp, vertical = 10.dp),
               horizontalArrangement = Arrangement.Start,
           ){
               Icon(
                   imageVector = Icons.AutoMirrored.Sharp.Logout,
                   contentDescription = "log out",
                   tint = Color.Red,
                   modifier = Modifier.size(20.dp)
               )
               Spacer(modifier = Modifier.width(10.dp))
               Text(
                   text = "Log Out",
                   style = MaterialTheme.typography.labelMedium,
                   color = Color.Red,
                   fontSize = 17.sp,
                   textAlign = TextAlign.Start,
               )
           }
        }
    }
}

@Composable
fun TextIconLayout(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
){
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            contentColor = MaterialTheme.colorScheme.surface
        )
    ){
        Row(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.onPrimaryContainer)
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = icon,
                contentDescription = "text",
                tint = MaterialTheme.colorScheme.surface,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.surface,
                fontSize = 17.sp
            )
        }
    }
}