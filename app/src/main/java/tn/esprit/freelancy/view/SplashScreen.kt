package tn.esprit.freelancy.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import tn.esprit.freelancy.session.PreferenceManager


import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import tn.esprit.freelancy.session.SessionManager

@Composable
fun SplashScreen(navController: NavHostController,  sessionManager: SessionManager) {
    val isLoggedIn by sessionManager.isLoggedIn.collectAsState(initial = false)
    val userEmail by sessionManager.userEmail.collectAsState(initial = null)


    /* LaunchedEffect(Unit) {
         // Delay to simulate splash duration
         delay(2000)

         // Check if a token exists in preferences
         val token = preferenceManager.getToken()
         if (token.isNullOrEmpty()) {
             val email = preferenceManager.getUsername() ?: ""
             println(email)
             navController.navigate("home/$email") {
                 popUpTo("splash") { inclusive = true }
             }
         } else {
             navController.navigate("login") {
                 popUpTo("splash") { inclusive = true }
             }
         }
     }*/
    LaunchedEffect(Unit) {
      if (isLoggedIn) {
            navController.navigate("home/${userEmail ?: ""}") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }
    // UI for Splash Screen
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
