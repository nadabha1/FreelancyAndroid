package tn.esprit.freelancy.view.projet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import tn.esprit.freelancy.viewModel.projet.ProjetViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(navController: NavController, projectId: String, viewModel: ProjetViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Fetch the project by its ID when this screen is opened
    LaunchedEffect(projectId) {
        viewModel.fetchProjectById(projectId)
    }
    val success by viewModel.success.collectAsState()

    val selectedProject by viewModel.selectedProject.collectAsState()
    var dialogMessage by remember { mutableStateOf<String?>(null) } // For error dialogs

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Project Details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF003F88))
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF001E6C))
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (selectedProject == null) {
                // Show loading indicator while fetching data
                CircularProgressIndicator(color = Color(0xFF1E88E5))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    // Project Title
                    Text(
                        text = selectedProject?.title.orEmpty(),
                        fontSize = 22.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Project Description
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF003F88)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Description:",
                                fontSize = 18.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = selectedProject?.description.orEmpty(),
                                fontSize = 16.sp,
                                color = Color.LightGray,
                                textAlign = TextAlign.Justify
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Project Details
                    Column(modifier = Modifier.fillMaxWidth()) {
                        DetailRow(label = "Technologies", value = selectedProject?.technologies.orEmpty())
                        DetailRow(label = "Budget", value = "${selectedProject?.budget} USD")
                        DetailRow(label = "Status", value = selectedProject?.status.orEmpty())
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Apply Button
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val result = viewModel.applyForProject(projectId, context = navController.context)
                                if (result.isSuccessful) {
                                    snackbarHostState.showSnackbar(
                                        message = "Votre postulation a été envoyée avec succès !",
                                        actionLabel = "OK"
                                    )
                                } else {
                                    snackbarHostState.showSnackbar(
                                        message = result.errorMessage!!,
                                        actionLabel = "OK"
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0056B3))
                    ) {
                        Text(text = "Postuler", color = Color.White, fontSize = 16.sp)
                    }

                }
            }
        }
    }

}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = Color.LightGray,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}
