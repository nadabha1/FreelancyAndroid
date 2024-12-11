package tn.esprit.freelancy.view.projet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import tn.esprit.freelancy.model.projet.Projet
import tn.esprit.freelancy.session.SessionManager
import tn.esprit.freelancy.viewModel.projet.ProjetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjetListScreen(
    navController: NavController,
    viewModel: ProjetViewModel,
    sessionManager: SessionManager,
    onAddProject: () -> Unit
) {

    val projects by viewModel.projects.collectAsState(initial = emptyList())
    var isLoading by remember { mutableStateOf(true) }
    val userEmail by sessionManager.userEmail.collectAsState(initial = null)

    val email = userEmail
    println("Email in ProjetListScreen: $email")
    // Fetch projects and update loading state
    LaunchedEffect(Unit) {
        viewModel.fetchProjects() // Trigger fetching projects
        isLoading = false
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Projects", color = Color.White) },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("profile/$email")
                    }) {
                        Icon(Icons.Filled.Person, contentDescription = "Profile", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Color(0xFF003F88))
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProject, containerColor = Color(0xFF0056B3)) {
                Icon(Icons.Filled.Add, contentDescription = "Add Project", tint = Color.White)
            }
        },
        bottomBar = {
            BottomNavigationBar(navController = navController) // Integrate Bottom Navigation Bar
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
                    Text("No projects available", color = Color.White)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(projects) { project ->
                        ProjectCard2(project, onClick = {
                            navController.navigate("project_detailEnt/${project._id}")
                        })
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectCard2(project: Projet, onClick: () -> Unit) {
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
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProjetListPreview() {
    val sampleProjects = listOf(
        Projet(
            _id = "1",
            title = "Build a small website",
            description = "Looking for a developer to create a small Django web app.",
            technologies = "Python, Django",
            budget = "500",
            duration = "2 weeks",
            status = "In Progress",
            userId = "1"
        ),

        Projet(
            _id = "2",
            title = "Android App Development",
            description = "Need an Android app for managing tasks.",
            technologies = "Kotlin, Jetpack Compose",
            budget = "1000",
            duration = "3 weeks",
            status = "To Do",
            userId = "2")
    )

    ProjetListScreen(
        navController = rememberNavController(),
        viewModel = ProjetViewModel(SessionManager(LocalContext.current)).apply {
            // Mock data for preview
            _projects.value = sampleProjects
        },
        sessionManager = SessionManager(LocalContext.current),
        onAddProject = {}
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Projects", Icons.Default.Home, "projects"),
        BottomNavItem("Freelancers", Icons.Default.Person, "ongoing_projects"),
        BottomNavItem("Add Project", Icons.Default.Add, "addProject"),
        BottomNavItem("Messages", Icons.Default.MailOutline, "messages"),
        BottomNavItem("Alerts", Icons.Default.Notifications, "alerts")
    )

    NavigationBar(
        containerColor = Color(0xFF003F88)
    ) {
        val currentDestination = navController.currentBackStackEntryAsState()?.value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination == item.route,
                onClick = { navController.navigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = if (currentDestination == item.route) Color.White else Color.LightGray
                    )
                },
                label = {
                    Text(text = item.title, color = if (currentDestination == item.route) Color.White else Color.LightGray)
                }
            )
        }
    }
}

data class BottomNavItem(val title: String, val icon: ImageVector, val route: String)
