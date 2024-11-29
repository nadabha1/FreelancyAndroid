package tn.esprit.freelancy.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import tn.esprit.freelancy.session.SessionManager

@Composable
fun SplashScreen(navController: NavHostController, sessionManager: SessionManager) {
    // Observe session state using Compose's state handling
    val isLoggedIn by sessionManager.isLoggedIn.collectAsState(initial = false)
    val userEmail by sessionManager.userEmail.collectAsState(initial = null)
    val userRole by sessionManager.userRole.collectAsState(initial = null)

    LaunchedEffect(Unit) {
        // Simulate a splash screen duration (e.g., 2 seconds)
        delay(2000)

        // Navigate based on session state
        if (isLoggedIn) {
            println("Role in SplashScreen login: $userRole")
            if (userRole == "Entrepreneur") {
                navController.navigate("projects") {
                    println("Email in SplashScreen: $userEmail")

                    // Clear the back stack so the user can't return to the splash screen
                    popUpTo("splash") { inclusive = true }
                }
            }
            else if (userRole == "Freelancer") {
                navController.navigate("homeC/$userEmail") {
                    println("Email in SplashScreen: $userEmail")
                }
            }

        } else {
            navController.navigate("login") {
                // Clear the back stack for the same reason
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    // UI for the Splash Screen
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Welcome to Freelancy",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF283593)
        )
    }
}
