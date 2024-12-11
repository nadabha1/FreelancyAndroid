package tn.esprit.freelancy.view.projet

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalContext
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
    notificationViewModel: NotificationViewModel,
    navController: NavController // Ajouter NavController pour gérer la navigation
) {
    val notifications by remember { notificationViewModel.notifications }
    val snackbarHostState = remember { SnackbarHostState() }

    // Fetch notifications
    LaunchedEffect(entrepreneurId) {
        notificationViewModel.fetchNotifications()
    }

    // Handle unread notifications
    val latestUnreadNotification = notifications.firstOrNull { it.status == "unread" }

    // Show Snackbar for the latest unread notification
    LaunchedEffect(latestUnreadNotification) {
        latestUnreadNotification?.let {
            snackbarHostState.showSnackbar(
                message = it.message,
                actionLabel = "Mark as Read",
                withDismissAction = true,
                duration = SnackbarDuration.Short
            )
            it._id?.let { id ->
                notificationViewModel.markNotificationAsRead(id)
                notificationViewModel.fetchNotifications()
            } ?: Log.e("NotificationScreen", "Notification ID is null for message: ${it.message}")
        }
    }

    // Dialog state for displaying notification details
    var selectedNotification by remember { mutableStateOf<Notification?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Notifications",
                        style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Action pour le bouton de retour
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Action supplémentaire */ }) {
                        Icon(
                            Icons.Filled.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF003F88))
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), // Ajout d'une marge autour de la liste
                verticalArrangement = Arrangement.spacedBy(12.dp) // Espacement vertical entre les cartes
            ) {
                itemsIndexed(notifications) { _, notification ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp), // Marges internes de chaque carte
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp) // Coins arrondis
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically, // Alignement des icônes et du texte
                            horizontalArrangement = Arrangement.spacedBy(12.dp) // Espacement horizontal entre les éléments
                        ) {
                            // Icône ou image de statut
                            Icon(
                                imageVector = if (notification.status == "unread") Icons.Default.Star else Icons.Default.CheckCircle,
                                contentDescription = "Status Icon",
                                tint = if (notification.status == "unread") Color(0xFF003F88) else Color.Gray,
                                modifier = Modifier.size(40.dp) // Taille de l'icône
                            )
                            // Texte de notification
                            Column(
                                modifier = Modifier.weight(1f) // Permet au texte de prendre tout l'espace disponible
                            ) {
                                Text(
                                    text = notification.message,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    color = if (notification.status == "unread") Color.Black else Color.Gray,
                                    maxLines = 1, // Limite à une ligne
                                    overflow = TextOverflow.Ellipsis // Ajoute des "..." si le texte est trop long
                                )

                            }
                        }
                    }

                    // Gérer l'ouverture du dialogue lorsqu'on clique sur la notification
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                selectedNotification = notification
                                showDialog = true
                            }
                    )
                }
            }
        }
    )

    // Affichage du dialogue avec les détails de la notification
    if (showDialog && selectedNotification != null) {
        selectedNotification?.let { notification ->
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Notification Details") },
                text = {
                    Column {
                        Text("Message: ${notification.message}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Status: ${notification.status}")
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            notification._id?.let { id ->
                                notificationViewModel.markNotificationAsRead(id) // Marquer comme lu
                                notificationViewModel.fetchNotifications()
                                showDialog = false
                            }
                        }
                    ) {
                        Text("Mark as Read")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNotificationScreen() {
    EntrepreneurNotificationScreen(
        entrepreneurId = "123",
        notificationViewModel = NotificationViewModel(
            NotificationRepository(RetrofitClient.projetApi),
            sessionManager = tn.esprit.freelancy.session.SessionManager(LocalContext.current)
        ),
        navController = NavController(LocalContext.current)
    )
}
