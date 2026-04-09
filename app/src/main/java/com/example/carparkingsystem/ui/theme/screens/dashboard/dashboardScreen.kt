package com.example.carparkingsystem.ui.theme.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(navController: NavController){
    val selectedItem = remember { mutableIntStateOf(0) }
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Parking Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Red,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.Magenta) {
                NavigationBarItem(
                    selected = selectedItem.intValue == 0,
                    onClick = { selectedItem.intValue = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedItem.intValue == 1,
                    onClick = { selectedItem.intValue = 1 },
                    icon = { Icon(Icons.Default.Info, contentDescription = "About") },
                    label = { Text("About") }
                )
                NavigationBarItem(
                    selected = selectedItem.intValue == 2,
                    onClick = { selectedItem.intValue = 2 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") }
                )
            }
        }
    )
    { padding ->
        Column(modifier = Modifier.padding(padding))
        {
            Text(
                text = "Smart Parking System",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth().padding(20.dp)
            ) {
                // Add card content here if needed
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    Dashboard(rememberNavController())
}
