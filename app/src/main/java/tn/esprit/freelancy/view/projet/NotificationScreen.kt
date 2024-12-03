package tn.esprit.freelancy.view.projet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.remote.projet.ProjetAPI
import tn.esprit.freelancy.repository.NotificationRepository
import tn.esprit.freelancy.viewModel.projet.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    notificationViewModel: NotificationViewModel
) {
    val entrepreneurId = "123" // Replace with actual entrepreneur ID
    // Hold notifications state
    val notifications by remember { notificationViewModel.notifications }

    // Snackbar state
    val snackbarHostState = remember { SnackbarHostState() }

    // Fetch notifications when screen is launched
    LaunchedEffect(entrepreneurId) {
        notificationViewModel.fetchNotifications()
    }

    // Displaying the Notifications
    LazyColumn {
        items(notifications) { notification ->
            // Display each notification in a List item
            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )

            // Show a Snackbar for unread notifications
            if (notification.status == "unread") {
                LaunchedEffect(notification) {
                    // Show Snackbar for unread notifications
                    snackbarHostState.showSnackbar(notification.message)
                }
            }
        }
    }

    // SnackbarHost to display Snackbar messages
    SnackbarHost(
        hostState = snackbarHostState
    )
}


@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    val notificationRepository = NotificationRepository(RetrofitClient.projetApi)
    NotificationScreen( notificationViewModel = NotificationViewModel(notificationRepository,sessionManager = tn.esprit.freelancy.session.SessionManager(LocalContext.current)))
}
