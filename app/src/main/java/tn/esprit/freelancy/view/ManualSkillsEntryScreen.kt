package tn.esprit.freelancy.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import tn.esprit.freelancy.R
import tn.esprit.freelancy.viewModel.CvViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualSkillsEntryScreen(
    viewModel: CvViewModel = viewModel(),
    navController: NavController,
    username: String
) {
    var skills by remember { mutableStateOf("") }
    val skillList = remember { mutableStateListOf<String>() }
    val userProfile = viewModel.userProfile.collectAsState().value
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

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
                text = "Add Your Skills",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
            )

            // Input Field for Adding Skills
            OutlinedTextField(
                value = skills,
                onValueChange = { input ->
                    if (input.length >= 30) { // Text field validation: max length 30 characters
                        skills = input
                    } else {
                        errorMessage = "Skill must be 30 characters or more."
                        showErrorDialog = true
                    }
                },
                label = { Text("Enter a Skill") },
                placeholder = { Text("e.g., Java, Python, Project Management") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF1E88E5),
                    unfocusedBorderColor = Color.Gray,
                    textColor = Color.White
                ),
                keyboardOptions = KeyboardOptions.Default
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add Skill Button
            Button(
                onClick = {
                    if (skills.isNotBlank()) {
                        skillList.add(skills.trim())
                        skills = "" // Clear the input field
                    } else {
                        errorMessage = "Skill cannot be empty."
                        showErrorDialog = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Add Skill", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Display Added Skills
            if (skillList.isNotEmpty()) {
                Text(
                    text = "Your Skills:",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    skillList.forEach { skill ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = skill, color = Color.White, fontSize = 16.sp)
                            IconButton(
                                onClick = { skillList.remove(skill) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                androidx.compose.material3.Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete Skill",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "No skills added yet. Use the field above to add skills.",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save and Continue Button
            Button(
                onClick = {
                    if (userProfile != null) {
                        viewModel.updateSkills(userProfile.idUser, skillList)
                        showSuccessDialog = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Save & Continue", color = Color.White)
            }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Success", fontWeight = FontWeight.Bold) },
            text = { Text("Skills have been saved successfully!") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.navigate("login") // Navigate to the next screen
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
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

