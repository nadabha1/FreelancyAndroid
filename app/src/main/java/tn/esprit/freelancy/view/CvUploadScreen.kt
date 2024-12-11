package tn.esprit.freelancy.view

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import tn.esprit.freelancy.viewModel.CvViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CvUploadScreen(
    viewModel: CvViewModel = viewModel(),
    navController: NavController,
    username: String
) {
    val uiState by viewModel.uiState.collectAsState()
    val userProfile = viewModel.userProfile.collectAsState().value
    var selectedFile by remember { mutableStateOf<File?>(null) }
    var skillsText by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val inputStream = context.contentResolver.openInputStream(uri)
                val tempFile = File(context.cacheDir, "temp_cv.pdf")
                inputStream?.use { input -> tempFile.outputStream().use { output -> input.copyTo(output) } }
                selectedFile = tempFile
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Upload Your CV",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
            )

            // Button to select a PDF file
            Button(
                onClick = { filePickerLauncher.launch("application/pdf") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select PDF File", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Upload button if a file is selected
            selectedFile?.let {
                Button(
                    onClick = {
                        if (userProfile != null) {
                            viewModel.uploadCv2(userProfile.idUser, it)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Upload CV", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Handling different UI states
            when (uiState) {
                is CvViewModel.UiState.Idle -> Text("Select a file to upload", color = Color.Gray)
                is CvViewModel.UiState.Loading -> CircularProgressIndicator(color = Color(0xFF1E88E5))
                is CvViewModel.UiState.Success -> {
                    val response = (uiState as CvViewModel.UiState.Success).response
                    if (skillsText.isEmpty()) {
                        // Populate skills only the first time after a successful response
                        skillsText = response.skills.joinToString(", ")
                    }
                    Text(
                        "Upload successful! Review and edit skills below:",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Editable TextField for skills
                    OutlinedTextField(
                        value = skillsText,
                        onValueChange = { skillsText = it },
                        label = { Text("Edit Skills") },
                        placeholder = { Text("e.g., Java, Python") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF1E88E5),
                            unfocusedBorderColor = Color.Gray,
                            textColor = Color.White
                        ),
                        keyboardOptions = KeyboardOptions.Default
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Button to update the profile with modified skills
                    Button(
                        onClick = {
                            if (userProfile != null) {
                                val updatedSkills = skillsText.split(",")
                                    .map { it.replace("\n", " ").trim() }
                                    .filter { it.isNotEmpty() } // Remove empty entries
                                viewModel.updateSkills(userProfile.idUser, updatedSkills)
                                showSuccessDialog = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Update Profile", color = Color.White)
                    }
                }

                is CvViewModel.UiState.Error -> {
                    val error = (uiState as CvViewModel.UiState.Error).message
                    errorMessage = error
                    showErrorDialog = true
                }
            }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Success", fontWeight = FontWeight.Bold) },
            text = { Text("Your profile has been updated successfully!") },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Success Icon",
                    tint = Color.Green
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.navigate("login") // Navigate to login
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    // Error Dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error", fontWeight = FontWeight.Bold, color = Color.Red) },
            text = { Text(errorMessage, color = Color.White) },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = "Error Icon",
                    tint = Color.Red
                )
            },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
