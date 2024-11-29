package tn.esprit.freelancy.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProjectView() {
    val viewModel: AddProjectViewModel = viewModel() // ViewModel for state management

    val statuses = listOf("To Do", "In Progress", "Completed") // Possible statuses
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add a New Project") },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Color.Blue)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Text(
                text = "Provide details about the project to attract the best freelancers.",
                fontSize = 16.sp,
                color = Color.Gray
            )

            // Project Title
            InputField(
                label = "Project Title",
                value = viewModel.projectTitle,
                onValueChange = { viewModel.projectTitle = it },
                leadingIcon = Icons.Default.AddCircle
            )

            // Project Description
            InputField(
                label = "Project Description",
                value = viewModel.projectDescription,
                onValueChange = { viewModel.projectDescription = it },
                leadingIcon = Icons.Default.AddCircle,
                isMultiline = true
            )

            // Technologies Field
            InputField(
                label = "Technologies",
                value = viewModel.projectTechnologies,
                onValueChange = { viewModel.projectTechnologies = it },
                leadingIcon = Icons.Default.Add
            )

            // Project Budget
            InputField(
                label = "Budget (in USD)",
                value = viewModel.projectBudget,
                onValueChange = { viewModel.projectBudget = it },
                leadingIcon = Icons.Default.AddCircle,
                keyboardType = KeyboardType.Number
            )

            // Calendar for Deadline Selection
            Column {
                Text("Select Deadline", color = Color.Gray, fontSize = 14.sp)
                OutlinedButton(onClick = { viewModel.showDatePicker = true }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(dateFormatter.format(viewModel.projectDeadline))
                }
            }

            if (viewModel.showDatePicker) {
                DatePickerDialog(
                    initialDate = viewModel.projectDeadline,
                    onDateSelected = { viewModel.projectDeadline = it },
                    onDismiss = { viewModel.showDatePicker = false }
                )
            }

            // Project Status Picker
            Column {
                Text("Project Status", color = Color.Gray, fontSize = 14.sp)
                DropdownMenu(
                    expanded = viewModel.statusMenuExpanded,
                    onDismissRequest = { viewModel.statusMenuExpanded = false }
                ) {
                    statuses.forEach { status ->
                        DropdownMenuItem(
                            onClick = {
                                viewModel.projectStatus = status
                                viewModel.statusMenuExpanded = false
                            },
                            text = { Text(status) }
                        )
                    }
                }
                OutlinedButton(onClick = { viewModel.statusMenuExpanded = true }) {
                    Text(viewModel.projectStatus.ifEmpty { "Select Status" })
                }
            }

            // Error Message
            if (viewModel.showError) {
                Text(
                    text = viewModel.errorMessage,
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }

            // Submit Button
            Button(
                onClick = { viewModel.submitProject() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Submit Project", color = Color.White)
                }
            }
        }
    }
}

// MARK: - Input Field Component
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: ImageVector,
    isMultiline: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = { Icon(leadingIcon, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isMultiline) 120.dp else 56.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = !isMultiline
        )
    }
}

// MARK: - Date Picker Dialog Component
@Composable
fun DatePickerDialog(
    initialDate: Date,
    onDateSelected: (Date) -> Unit,
    onDismiss: () -> Unit
) {
    // A placeholder dialog function. Use a library like MaterialDatePicker for real implementation.
    onDismiss() // Close the dialog for now
}

// MARK: - ViewModel
class AddProjectViewModel : ViewModel() {
    var projectTitle by mutableStateOf("")
    var projectDescription by mutableStateOf("")
    var projectTechnologies by mutableStateOf("")
    var projectBudget by mutableStateOf("")
    var projectDeadline by mutableStateOf(Date())
    var projectStatus by mutableStateOf("")
    var showDatePicker by mutableStateOf(false)
    var statusMenuExpanded by mutableStateOf(false)
    var showError by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    fun submitProject() {
        if (projectTitle.isEmpty() || projectDescription.isEmpty()) {
            showError = true
            errorMessage = "All fields are required."
        } else {
            isLoading = true
            // Simulate API call
        }
    }
}
