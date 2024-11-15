package tn.esprit.freelancy.view
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import tn.esprit.freelancy.R
import tn.esprit.freelancy.viewModel.HomeViewModel
@Composable
fun ProfileContent(navController: NavHostController, email: String, viewModel: HomeViewModel) {
    val userProfile = viewModel.userProfile.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()

    // État pour afficher ou cacher la popup de confirmation
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    // Fetch user profile on screen load
    LaunchedEffect(Unit) {
        viewModel.fetchUser(email)
    }

    if (errorMessage.value != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = errorMessage.value!!, color = MaterialTheme.colorScheme.error)
        }
    } else if (userProfile.value == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            userProfile.value?.avatarUrl?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Avatar",
                    modifier = Modifier.size(120.dp).clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = userProfile.value?.username ?: "No Name",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            MenuOption(iconId = Icons.Default.Edit, text = "Edit Profile") {
                userProfile.value?.let {
                    navController.navigate("edit_profile/${email}")
                } ?: run {
                    println("Error: User profile is null.")
                }
            }

            MenuOption(iconId = Icons.Default.Lock, text = "Change Password") { /* Navigate to Change Password */ }
            MenuOption(iconId = Icons.Default.Info, text = "Information") { /* Navigate to Information */ }
            MenuOption(iconId = Icons.Default.Build, text = "Update") { /* Navigate to Update */ }
            MenuOption(iconId = Icons.Default.ExitToApp, text = "Log out") { /* Perform Log out */ }

            Spacer(modifier = Modifier.height(16.dp))

            // Bouton pour supprimer le compte
            Button(
                onClick = { showDeleteConfirmation = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Account")
            }

            // Popup de confirmation
            if (showDeleteConfirmation) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmation = false },
                    confirmButton = {
                        TextButton(onClick = {
                            userProfile.value?.let {
                                viewModel.deleteAccount(it.idUser) // Appel à la fonction de suppression
                                showDeleteConfirmation = false
                                navController.navigate("login") {
                                    popUpTo("profile") { inclusive = true } // Supprime la pile d'écrans jusqu'à la page de profil
                                }                            }
                        }) {
                            Text("Confirm", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteConfirmation = false }) {
                            Text("Cancel")
                        }
                    },
                    title = { Text("Delete Account") },
                    text = { Text("Are you sure you want to delete your account? This action cannot be undone.") }
                )
            }


        }
    }
}

@Composable
fun MenuOption(iconId: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = iconId,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
    }

}

