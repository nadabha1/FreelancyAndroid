package tn.esprit.freelancy.view.projet

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import tn.esprit.freelancy.viewModel.projet.NotificationViewModel
import tn.esprit.freelancy.model.projet.Notification
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.repository.NotificationRepository
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntrepreneurNotificationScreen(
    entrepreneurId: String,
    notificationViewModel: NotificationViewModel
) {
    // Collect notifications from ViewModel
    val notifications by remember { notificationViewModel.notifications }

    // Define SnackbarHostState for showing notifications
    val snackbarHostState = remember { SnackbarHostState() }

    // Fetch notifications on screen launch
    LaunchedEffect(entrepreneurId) {
        notificationViewModel.fetchNotifications()
    }

    // Handle unread notifications
    val latestUnreadNotification = notifications.firstOrNull { it.status == "unread" }

    // Show Snackbar for the latest unread notification
    LaunchedEffect(latestUnreadNotification) {
        latestUnreadNotification?.let {
            snackbarHostState.showSnackbar(
                it.message, // Directly passing the message string
                actionLabel = "Mark as Read",  // Action label for Snackbar
                withDismissAction = true,     // Whether the Snackbar can be dismissed manually
                duration = SnackbarDuration.Short // Duration for the Snackbar to appear
            )

            println("Notification marked as read: ${it._id}")
            // Ensure the notificationId is not null before calling markNotificationAsRead
            it._id?.let { id ->
                notificationViewModel.markNotificationAsRead(id)

            } ?: run {
                Log.e("NotificationScreen", "Notification ID is null for message: ${it.message}")
            }
        }
    }

    // Scaffold with a top bar and a list of notifications
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Entrepreneur Notifications") },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF003F88))
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = {
            // Use LazyColumn to display notifications
            LazyColumn {
                itemsIndexed(notifications) { index, notification ->
                    // Card for each notification with styling for readability
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Icon based on unread status
                            Icon(
                                imageVector = if (notification.status == "unread") Icons.Default.Star else Icons.Default.CheckCircle,
                                contentDescription = "Status Icon",
                                tint = if (notification.status == "unread") Color(0xFF003F88) else Color.Gray,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = notification.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (notification.status == "unread") Color.Black else Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNotificationScreen() {
    EntrepreneurNotificationScreen(
        entrepreneurId = "123",
        notificationViewModel = NotificationViewModel(
            NotificationRepository(RetrofitClient.projetApi),
            sessionManager = tn.esprit.freelancy.session.SessionManager(LocalContext.current)
        )
    )
}
