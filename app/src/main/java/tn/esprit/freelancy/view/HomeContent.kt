package tn.esprit.freelancy.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import tn.esprit.freelancy.viewModel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(navController: NavHostController, username: String, viewModel: HomeViewModel) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, username)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Welcome to the Home Screen, $username!", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, email: String) {
    val currentDestination = navController.currentBackStackEntryAsState()?.value?.destination?.route

    NavigationBar {
        // Home Navigation Item
        NavigationBarItem(
            selected = currentDestination == "home/$email",
            onClick = { navController.navigate("home/$email") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )

        // Profile Navigation Item
        NavigationBarItem(
            selected = currentDestination == "profile/$email",
            onClick = { navController.navigate("profile/$email") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}
