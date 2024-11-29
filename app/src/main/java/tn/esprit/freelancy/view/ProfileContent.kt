package tn.esprit.freelancy.view
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import tn.esprit.freelancy.R
import tn.esprit.freelancy.model.user.UserProfileComplet
import tn.esprit.freelancy.session.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, user: UserProfileComplet,sessionManager: SessionManager) {
    // Check if critical user fields are null
    if (user.username.isEmpty()) {
        Text(text = "No user information available", color = MaterialTheme.colorScheme.error)
        return
    }
    val role = sessionManager.getSession()?.role
    println("Role in ProfileScreennnnnnnnn: $role")
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Toolbar with Back Button
        TopAppBar(
            title = { Text("Profile", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF1E88E5))
        )
        // Render the user profile if fields are valid
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F6F6))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Toolbar with Back Button


                // Profile Image Section
                Image(
                    painter = rememberAsyncImagePainter(
                        model = user.avatarUrl ?: R.drawable.profile_placeholder
                    ),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(60.dp))
                        .background(Color.Gray)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // User Details Section
                Text(
                    text = user.username ?: "No username",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E88E5)
                )

                Text(
                    text = user.email ?: "No email",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))
                println(user.country)
                println(user.idUser)
                // Details Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ProfileDetailRow(
                            icon = Icons.Default.Person,
                            label = "Date of Birth",
                            value1 = user.dateOfBirth ?: "Not provided"
                        )
                        ProfileDetailRow(
                            icon = Icons.Default.Home,
                            label = "Country",
                            value1 = user.country ?: "Not provided"

                        )
                        ProfileDetailRow(
                            icon = Icons.Default.Settings,
                            label = "Role",
                            value1 = role ?: "No role assigned"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                // Log out and Delete Account Buttons
                Button(
                    onClick = {
                        sessionManager.clearSession()
                        navController.navigate("login")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Log Out", color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { /* Handle account deletion */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete Account", color = Color.White)
                }
            }
        }
    }
}
@Composable
fun ProfileDetailRow(icon: ImageVector, label: String, value1: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF1E88E5),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, fontSize = 16.sp, color = Color.Gray)
        }
        Text(text = value1, fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val dummyUser = UserProfileComplet(
        username = "nada",
        email = "nada@example.com",
        avatarUrl = "dcfvghj", // Replace with valid URL for testing
        idUser = "123",
        role = "Freelancer"

    )
    ProfileScreen(navController = rememberNavController(), user = dummyUser, sessionManager = SessionManager(LocalContext.current))
}
