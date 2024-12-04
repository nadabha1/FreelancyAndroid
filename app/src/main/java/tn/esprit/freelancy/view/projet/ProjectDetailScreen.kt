package tn.esprit.freelancy.view.projet

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import tn.esprit.freelancy.model.projet.Project
import tn.esprit.freelancy.viewModel.projet.ProjetViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(navController: NavController, projectId: String, viewModel: ProjetViewModel) {
    // Fetch the project by its ID when this screen is opened
    LaunchedEffect(projectId) {
        viewModel.fetchProjectById(projectId)
    }

    val selectedProject by viewModel.selectedProject.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Project Details", color = Color.White) },
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
                // Show loading indicator or error message if the project is not found
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                // Display the project details
                Text(
                    text = "Title: ${selectedProject?.title}",
                    fontSize = 18.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Description: ${selectedProject?.description}",
                    fontSize = 16.sp,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Technologies: ${selectedProject?.technologies}",
                    fontSize = 14.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Budget: ${selectedProject?.budget} USD",
                    fontSize = 14.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Status: ${selectedProject?.status}",
                    fontSize = 14.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Button to apply for the project
                Button(
                    onClick = { viewModel.applyForProject(projectId, context = navController.context ) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0056B3))
                ) {
                    Text(text = "Postuler", color = Color.White)
                }
            }
        }
    }
}
