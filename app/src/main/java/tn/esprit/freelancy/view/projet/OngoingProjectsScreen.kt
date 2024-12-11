package tn.esprit.freelancy.view.projet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import tn.esprit.freelancy.model.projet.Project
import tn.esprit.freelancy.model.projet.ProjectResponse
import tn.esprit.freelancy.session.SessionManager
import tn.esprit.freelancy.viewModel.projet.ProjetViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OngoingProjectsScreen(navController: NavController, viewModel: ProjetViewModel,sessionManager: SessionManager) {
    val projects by viewModel.projectsIa.collectAsState(initial = emptyList())
    var isLoading by remember { mutableStateOf(true) }
    val userId = sessionManager.getUserId()


    // Fetch projects assuming userId is available
    LaunchedEffect(Unit) {
        viewModel.fetchProjectByIa()
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "List of project ", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF003F88))
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Action for adding a project (navigate or show dialog)
            }, containerColor = Color(0xFF0056B3)) {
                Icon(Icons.Filled.Add, contentDescription = "Add Project", tint = Color.White)
            }
        },
        bottomBar = {
            BottomNavigationBar(navController = navController) // Bottom Navigation Bar (optional)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF001E6C))
                .padding(16.dp)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (projects.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("loading... ", color = Color.White)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(projects) { project ->
                        ProjetCard(project, onClick = {
                            navController.navigate("project_detail/${project.id}")
                        })

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
@Composable
fun ProjetCard(project: Project, onClick: () -> Unit) {
    // Convert score to percentage
    val scorePercentage = (project.score * 100).toInt()

    // Determine color based on score
    val scoreColor = if (scorePercentage >= 50) Color.Green else Color.Red

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0056B3)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Project Title
            Text(
                text = project.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Project Description
            Text(
                text = project.description,
                color = Color.LightGray,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Technologies Used
            Text(
                text = "Technologies: ${project.technologies}",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Additional Info (Budget & Status)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Budget: ${project.budget} USD",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Status: ${project.status}",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }

                // Show score as percentage and color it
                Text(
                    text = "Score: $scorePercentage%",
                    color = scoreColor, // Color changes based on score
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
