package tn.esprit.freelancy.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import tn.esprit.freelancy.model.Client
import tn.esprit.freelancy.model.Job
import tn.esprit.freelancy.viewModel.HomeViewModel
import tn.esprit.freelancy.viewModel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController, email: String, viewModel: HomeViewModel) {
    val jobs = listOf(
        Job(
            title = "Need a super tiny Android APP / just day and time",
            description = "My developer account at Google is at risk of being closed because it's not being used actively...",
            budget = "$100",
            level = "Entry level",
            tags = listOf("Android"),
            client = Client(paymentVerified = true, spent = "$600+", location = "Germany"),
            proposals = "20 to 50"
        )
    )
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
                    navController.navigate("profile/$email")
                }) {
                    androidx.compose.material3.Icon(Icons.Filled.Person, contentDescription = "Home", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.largeTopAppBarColors(containerColor =Color(0xFF003F88))

        )


        Column(
        modifier = Modifier
            .fillMaxSize()
            .background( Color(0xFF001E6C))
            .padding(16.dp)
    ) {
        // Header


        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        var query by remember { mutableStateOf("") }
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

        // Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /* Handle Best Matches */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0056B3)) // Dark blue button
            ) {
                Text(text = "Best Matches", color = Color.White)
            }
            Button(
                onClick = { /* Handle Most Recent */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003F88)) // Slightly lighter blue button
            ) {
                Text(text = "Most Recent", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Job List
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(jobs) { job ->
                JobCard(job)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    }
}

@Composable
fun JobCard(job: Job) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0056B3)), // Card background color (blue)
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Job Title
            Text(
                text = job.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Job Description
            Text(
                text = job.description,
                color = Color.LightGray,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Tags
            Row(modifier = Modifier.padding(top = 8.dp)) {
                job.tags.forEach { tag ->
                    Text(
                        text = tag,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(Color(0xFF007BFF), RoundedCornerShape(8.dp)) // Blue tag background
                            .padding(4.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            // Additional Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    if (job.client.paymentVerified) {
                        Text(
                            text = "Payment Verified",
                            color = Color.Green,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = job.client.spent,
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                    Text(
                        text = job.client.location,
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = "Proposals: ${job.proposals}",
                    color = Color.LightGray,
                    fontSize = 12.sp
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
        viewModel = HomeViewModel() // Replace with your actual view model if needed
    )
}
