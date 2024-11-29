package tn.esprit.freelancy.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import tn.esprit.freelancy.model.user.UserProfile1
import tn.esprit.freelancy.repository.AuthRepository
import tn.esprit.freelancy.viewModel.UpdateProfileViewModel
import tn.esprit.freelancy.viewModel.UpdateProfileViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavHostController,
    email: String,
    userRepository: AuthRepository,
    onSaveSuccess: () -> Unit
) {
    // Instanciation du ViewModel avec une factory
    val viewModel: UpdateProfileViewModel = viewModel(
        factory = UpdateProfileViewModelFactory(userRepository)
    )

    // États locaux pour le formulaire
    var nameState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }

    // Récupération des données utilisateur
    val userProfile = viewModel.userProfile.collectAsState().value
    val updateSuccess = viewModel.updateSuccess.collectAsState().value
    val errorMessage = viewModel.errorMessage.collectAsState().value

    // Charger les données utilisateur à partir de l'email
    LaunchedEffect(email) {
        viewModel.fetchUser(email)
    }

    // Mise à jour des champs locaux lorsque `userProfile` est mis à jour
    LaunchedEffect(userProfile) {
        userProfile?.let {
            nameState = it.username ?: "" // Si `username` est null, utilisez une chaîne vide
            emailState = it.email ?: "" // Si `email` est null, utilisez une chaîne vide
        } ?: viewModel.setErrorMessage("Error: User profile is null.")
    }

    // Gérer le succès de la mise à jour
    LaunchedEffect(updateSuccess) {
        if (updateSuccess == true) {
            onSaveSuccess()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Home, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = nameState ?: "", // Utiliser une chaîne vide si `nameState` est null
                onValueChange = { nameState = it },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = emailState ?: "", // Utiliser une chaîne vide si `emailState` est null
                onValueChange = { emailState = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (nameState.isNullOrBlank() || emailState.isNullOrBlank()) {
                        viewModel.setErrorMessage("Fields cannot be empty")
                        return@Button
                    }

                    userProfile?.let {
                        val updatedProfile = UserProfile1(
                            idUser = it.idUser,
                            username = nameState,
                            email = emailState
                        )
                        viewModel.updateUserProfile(updatedProfile, it)
                    } ?: viewModel.setErrorMessage("User profile is null. Please try again.")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Affichage des erreurs
            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

        }
    }
}

fun Icon(icon: ImageVector, contentDescription: String, tint: Color, modifier: Modifier) {
    // Implémentation vide ou à adapter selon vos besoins
}
