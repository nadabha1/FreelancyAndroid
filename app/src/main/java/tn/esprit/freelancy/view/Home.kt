package tn.esprit.freelancy.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import tn.esprit.freelancy.model.projet.Project
import tn.esprit.freelancy.model.user.Client
import tn.esprit.freelancy.model.user.Job
import tn.esprit.freelancy.session.SessionManager
import tn.esprit.freelancy.viewModel.HomeViewModel
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Home(navController: NavController, email: String, viewModel: HomeViewModel, sessionManager: SessionManager) {
//
//    val projects by viewModel.projectsIa.collectAsState(initial = emptyList())
//    var filteredProjects by remember { mutableStateOf(projects) }
//    var isLoading by remember { mutableStateOf(true) }
//    var isBestMatch by remember { mutableStateOf(false) } // Track Best Matches vs Most Recent
//    var isBestMatchSelected by remember { mutableStateOf(false) } // Track if Best Match button is selected
//    var isMostRecentSelected by remember { mutableStateOf(false) } // Track if Most Recent button is selected
//    val userId = sessionManager.getUserId()
//    val userEmail by sessionManager.userEmail.collectAsState(initial = null)
//
//    val email2 = userEmail
//    // Fetch projects assuming userId is available
//    LaunchedEffect(Unit) {
//        viewModel.fetchProjectByIa()
//        isLoading = false
//    }
//
//    // Apply filtering logic based on isBestMatch
//    LaunchedEffect(isBestMatch) {
//        filteredProjects = if (isBestMatch) {
//            // Filter projects with score > 50%
//            projects.filter { it.score >= 0.5 }
//        } else {
//            // Show all projects (Most Recent)
//            projects
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
//        TopAppBar(
//            title = { Text(text = "Jobs", color = Color.White) },
//            actions = {
//                // Profile Button
//                IconButton(onClick = {
//                    // Navigate to Profile Screen
//                    navController.navigate("profile/$email2")
//                }) {
//                    androidx.compose.material3.Icon(Icons.Filled.Person, contentDescription = "Home", tint = Color.White)
//                }
//            },
//            colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Color(0xFF003F88))
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color(0xFF001E6C))
//                .padding(16.dp)
//        ) {
//            // Header
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Search Bar
//            var query by remember { mutableStateOf("") }
//            TextField(
//                value = query,
//                onValueChange = { query = it },
//                placeholder = { Text("Search for jobs", color = Color.Gray) },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color(0xFF007BFF), RoundedCornerShape(8.dp)) // Blue background for search
//                    .padding(12.dp),
//                colors = TextFieldDefaults.textFieldColors(
//                    textColor = Color.White,
//                    cursorColor = Color.White,
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent
//                )
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Tabs
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Button(
//                    onClick = {
//                        isBestMatch = true
//                        isBestMatchSelected = true
//                        isMostRecentSelected = false // Reset Most Recent selection
//                    },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = if (isBestMatchSelected) Color(0xFF0056B3) else Color(0xFF003F88)
//                    ) // Change color when selected
//                ) {
//                    Text(text = "Best Matches", color = Color.White)
//                }
//                Button(
//                    onClick = {
//                        isBestMatch = false
//                        isMostRecentSelected = true
//                        isBestMatchSelected = false // Reset Best Match selection
//                    },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = if (isMostRecentSelected) Color(0xFF0056B3) else Color(0xFF003F88)
//                    ) // Change color when selected
//                ) {
//                    Text(text = "Most Recent", color = Color.White)
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Job List
//            LazyColumn(modifier = Modifier.fillMaxSize()) {
//                if (isLoading) {
//                    item {
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            CircularProgressIndicator()
//                        }
//                    }
//                } else if (filteredProjects.isEmpty()) {
//                    item {
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text("No ongoing projects", color = Color.White)
//                        }
//                    }
//                } else {
//                    items(filteredProjects) { project ->
//                        ProjetCard(project, onClick = {
//                            // Handle card click
//                        })
//
//                        Spacer(modifier = Modifier.height(16.dp))
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ProjetCard(project: Project, onClick: () -> Unit) {
//    // Convert score to percentage
//    val scorePercentage = (project.score * 100).toInt()
//
//    // Determine color based on score
//    val scoreColor = if (scorePercentage >= 50) Color.Green else Color.Red
//
//    Card(
//        shape = RoundedCornerShape(8.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF0056B3)),
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .clickable { onClick() },
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            // Project Title
//            Text(
//                text = project.title,
//                color = Color.White,
//                fontWeight = FontWeight.Bold,
//                fontSize = 16.sp,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//
//            // Project Description
//            Text(
//                text = project.description,
//                color = Color.LightGray,
//                fontSize = 14.sp,
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//
//            // Technologies Used
//            Text(
//                text = "Technologies: ${project.technologies}",
//                color = Color.White,
//                fontSize = 12.sp,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//
//            // Additional Info (Budget & Status)
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 8.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column {
//                    Text(
//                        text = "Budget: ${project.budget} USD",
//                        color = Color.LightGray,
//                        fontSize = 12.sp
//                    )
//                    Text(
//                        text = "Status: ${project.status}",
//                        color = Color.LightGray,
//                        fontSize = 12.sp
//                    )
//                }
//
//                // Show score as percentage and color it
//                Text(
//                    text = "Score: $scorePercentage%",
//                    color = scoreColor, // Color changes based on score
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 16.sp,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController, email: String, viewModel: HomeViewModel, sessionManager: SessionManager) {

    val projects by viewModel.projectsIa.collectAsState(initial = emptyList())
    var filteredProjects by remember { mutableStateOf(projects) }
    var isLoading by remember { mutableStateOf(true) }
    var isBestMatch by remember { mutableStateOf(false) } // Track Best Matches vs Most Recent
    var isBestMatchSelected by remember { mutableStateOf(false) } // Track if Best Match button is selected
    var isMostRecentSelected by remember { mutableStateOf(false) } // Track if Most Recent button is selected
    val userId = sessionManager.getUserId()
    val userEmail by sessionManager.userEmail.collectAsState(initial = null)
    val email2 = userEmail

    // Fetch projects assuming userId is available
    LaunchedEffect(Unit) {
        viewModel.fetchProjectByIa()
        isLoading = false
    }

    // Apply filtering logic based on isBestMatch
    LaunchedEffect(isBestMatch) {
        filteredProjects = if (isBestMatch) {
            // Filter projects with score > 50%
            projects.filter { it.score >= 0.5 }
        } else {
            // Show all projects (Most Recent)
            projects
        }
    }

    // Search functionality
    var query by remember { mutableStateOf("") }

    // Update filteredProjects based on the search query
    LaunchedEffect(query) {
        filteredProjects = if (query.isNotEmpty()) {
            projects.filter {
                it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true)
            }
        } else {
            // If the query is empty, show all projects or filter based on Best Match
            if (isBestMatch) {
                projects.filter { it.score >= 0.5 }
            } else {
                projects
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(text = "Jobs", color = Color.White) },
            actions = {
                // Profile Button
                IconButton(onClick = {
                    // Navigate to Profile Screen
                    navController.navigate("profile/$email2")
                }) {
                    androidx.compose.material3.Icon(Icons.Filled.Person, contentDescription = "Home", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Color(0xFF003F88))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF001E6C))
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            TextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search for jobs", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF007BFF), RoundedCornerShape(8.dp)) // Blue background for search
                    .padding(12.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs for filtering (Best Matches vs Most Recent)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        isBestMatch = true
                        isBestMatchSelected = true
                        isMostRecentSelected = false // Reset Most Recent selection
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isBestMatchSelected) Color(0xFF0056B3) else Color(0xFF003F88)
                    ) // Change color when selected
                ) {
                    Text(text = "Best Matches", color = Color.White)
                }
                Button(
                    onClick = {
                        isBestMatch = false
                        isMostRecentSelected = true
                        isBestMatchSelected = false // Reset Best Match selection
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMostRecentSelected) Color(0xFF0056B3) else Color(0xFF003F88)
                    ) // Change color when selected
                ) {
                    Text(text = "Most Recent", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Job List
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else if (filteredProjects.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No ongoing projects", color = Color.White)
                        }
                    }
                } else {
                    items(filteredProjects) { project ->
                        ProjetCard(project, onClick = {
                            // Handle card click
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
    val scorePercentage = (project.score * 100).toInt()
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
            Text(
                text = project.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = project.description,
                color = Color.LightGray,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = "Technologies: ${project.technologies}",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

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

                Text(
                    text = "Score: $scorePercentage%",
                    color = scoreColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val sampleJobs = listOf(
        Job(
            title = "Need a super tiny Android APP / just day and time",
            description = "My developer account at Google is at risk of being closed because it's not being used actively...",
            budget = "$100",
            level = "Entry level",
            tags = listOf("Android"),
            client = Client(paymentVerified = true, spent = "$600+", location = "Germany"),
            proposals = "20 to 50"
        ),
        Job(
            title = "Build a small website with Django",
            description = "Looking for a Django developer to create a small web app...",
            budget = "$500",
            level = "Intermediate",
            tags = listOf("Python", "Django"),
            client = Client(paymentVerified = true, spent = "$2000+", location = "USA"),
            proposals = "5 to 10"
        )
    )

    Home(
        navController = rememberNavController(),
        email = "example@example.com",
        viewModel = HomeViewModel(sessionManager = SessionManager(LocalContext.current)),
        SessionManager(LocalContext.current)    )
}
