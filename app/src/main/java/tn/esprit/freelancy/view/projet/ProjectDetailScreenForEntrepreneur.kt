package tn.esprit.freelancy.view.projet

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import tn.esprit.freelancy.model.projet.Project
import tn.esprit.freelancy.model.projet.Projet
import tn.esprit.freelancy.session.SessionManager
import tn.esprit.freelancy.viewModel.HomeViewModel
import tn.esprit.freelancy.viewModel.HomeViewModelFactory
import tn.esprit.freelancy.viewModel.projet.ProjetViewModel
import androidx.compose.runtime.collectAsState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreenForEntrepreneur(
    navController: NavController,
    projectId: String,
    viewModel: ProjetViewModel
) {
    val selectedProject by viewModel.selectedProject.collectAsState()
    val freelancers by viewModel.freelancer.collectAsState()
    val showError by viewModel.showError.collectAsState()
    val applicationStatuses by viewModel.applicationStatuses.collectAsState()
    val applicationStatus by viewModel.statusMap.collectAsState()


    LaunchedEffect(projectId, freelancers) {
        viewModel.fetchProjectById(projectId)
        // Accéder aux statuts des freelances
        freelancers.forEach { freelancer ->
            viewModel.fetchApplicationStatus(freelancer.idUser, projectId)
            val status = applicationStatus[freelancer.idUser] ?: "Pending" // Utilisation correcte de applicationStatus
            // Vous pouvez maintenant traiter le statut de chaque freelance ici
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Project Details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF003F88))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (selectedProject == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Text(
                    text = selectedProject?.title ?: "Untitled Project",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003F88)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Description: ${selectedProject?.description}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Freelancers:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003F88)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Liste des freelancers
                if (freelancers.isEmpty()) {
                    Text(
                        text = "No freelancers have applied yet.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                } else {
                    freelancers.forEach { freelancer ->
                        viewModel.fetchApplicationStatus(freelancer.idUser, projectId)
                        println("status  $applicationStatus ")

                        val status = applicationStatus[freelancer.idUser] ?: "Pending" // Utilisation correcte de applicationStatus
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = when (status) {
                                    "Accepted" -> Color(0xFFD4EDDA) // Vert pour "Accepted"
                                    "Rejected" -> Color(0xFFF8D7DA) // Rouge pour "Rejected"
                                    else -> Color(0xFFE8F0F2) // Par défaut "Pending"
                                }
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Name: ${freelancer.username}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Skills: ${freelancer.skills}",
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                // Afficher le statut de l'application
                                Text(
                                    text = "Status: $status",
                                    fontSize = 14.sp,
                                    color = when (status) {
                                        "Accepted" -> Color(0xFF4CAF50)
                                        "Rejected" -> Color(0xFFF44336)
                                        else -> Color.Gray
                                    },
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Boutons d'action
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    when (status) {
                                        "Pending" -> {
                                            IconButton(onClick = {
                                                viewModel.acceptApplication(freelancer.idUser, projectId)
                                                viewModel.fetchApplicationStatus(freelancer.idUser, projectId)

                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Accept",
                                                    tint = Color(0xFF4CAF50)
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(8.dp))

                                            IconButton(onClick = {
                                                viewModel.rejectApplication(freelancer.idUser, projectId)
                                                viewModel.fetchApplicationStatus(freelancer.idUser, projectId)
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Reject",
                                                    tint = Color(0xFFF44336)
                                                )
                                            }
                                        }
                                        "Accepted" -> {
                                            IconButton(onClick = {}, modifier = Modifier.alpha(0.3f)) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Accept",
                                                    tint = Color(0xFF4CAF50)
                                                )
                                            }
                                        }
                                        "Rejected" -> {
                                            IconButton(onClick = {}, modifier = Modifier.alpha(0.3f)) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Reject",
                                                    tint = Color(0xFFF44336)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }

                // Afficher un message d'erreur si nécessaire
                if (showError?.isNotEmpty() == true) {
                    AlertDialog(
                        onDismissRequest = { viewModel.dismissError() },
                        title = { Text("Error") },
                        text = { Text(showError!!) },
                        confirmButton = {
                            TextButton(onClick = { viewModel.dismissError() }) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewProjectDetailScreenForEntrepreneur() {
    // Mock Data
    val mockFreelancers = listOf(
        Freelancer(idUser = "1", username = "Alice", skills = "Kotlin, Compose"),
        Freelancer(idUser = "2", username = "Bob", skills = "Java, Android"),
        Freelancer(idUser = "3", username = "Charlie", skills = "UI/UX Design")
    )

    val mockProject = Projet(
        _id = "123",
        title = "Build an Android App",
        description = "A project to create a user-friendly Android application.",
        technologies = "nadouu",
        budget = "",
        duration = "dfgh",
        status = "Pending",
        userId = "215"

    )

    val mockStatuses = mapOf(
        "1" to "Pending",
        "2" to "Accepted",
        "3" to "Rejected"
    )

    ProjectDetailScreenForEntrepreneur(
        navController = NavController(LocalContext.current),
        projectId = "123",
        viewModel = ProjetViewModel(sessionManager = SessionManager(LocalContext.current))
    )
}

class Freelancer(idUser: String, username: String, skills: String) {

}
