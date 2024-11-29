package tn.esprit.freelancy.view.projet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import tn.esprit.freelancy.model.projet.Projet
import tn.esprit.freelancy.session.SessionManager
import tn.esprit.freelancy.view.DatePickerDialog
import tn.esprit.freelancy.viewModel.projet.ProjetViewModel
import tn.esprit.freelancy.viewModel.projet.ProjetViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProjectScreen(
    navController: NavController,
    sessionManager: SessionManager,
    onProjectAdded: () -> Unit
) {
    val factory = ProjetViewModelFactory(sessionManager)
    val viewModel: ProjetViewModel = viewModel(factory = factory)
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var technologies by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(Date()) }
    var status by remember { mutableStateOf("To Do") }
    val userId = sessionManager.getSession()?.id
    println("User ID in AddProjectScreen: $userId")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Project", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Color(0xFF003F88))
            )
        }

    ) {
        innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Project Title
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Project Title", color = Color.LightGray) },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFF003F88), textColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            )

            // Project Description
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Project Description", color = Color.LightGray) },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFF003F88), textColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            )

            // Technologies
            TextField(
                value = technologies,
                onValueChange = { technologies = it },
                label = { Text("Technologies", color = Color.LightGray) },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFF003F88), textColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            )

            // Budget
            TextField(
                value = budget,
                onValueChange = { budget = it },
                label = { Text("Budget (in USD)", color = Color.LightGray) },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFF003F88), textColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            )

            // Deadline Selector
            DatePicker(selectedDate) { newDate ->
                selectedDate = newDate
            }

            // Status Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatusRadioButton("To Do", status) { status = it }
                StatusRadioButton("In Progress", status) { status = it }
                StatusRadioButton("Completed", status) { status = it }
            }

            // Submit Button
            Button(
                onClick = {
                    viewModel.addProject(
                        Projet(
                            title = title,
                            description = description,
                            technologies = technologies,
                            budget = budget,
                            duration = selectedDate.toString(),
                            status = status,
                            userId = userId ?: ""
                        )
                    )
                    onProjectAdded()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0056B3)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Submit Project", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun StatusRadioButton(value: String, selectedValue: String, onSelected: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = value == selectedValue,
            onClick = { onSelected(value) },
            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF0056B3), unselectedColor = Color.LightGray)
        )
        Text(value, color = if (value == selectedValue) Color.White else Color.LightGray)
    }
}

@Composable
fun DatePicker(selectedDate: Date, onDateSelected: (Date) -> Unit) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    calendar.time = selectedDate

    // Date picker dialog
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val newCalendar = Calendar.getInstance()
            newCalendar.set(year, month, dayOfMonth)
            onDateSelected(newCalendar.time)
            expanded = false
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column {
        OutlinedButton(
            onClick = { expanded = true },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF003F88))
        ) {
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate)
            Text("Select Deadline: $formattedDate", color = Color.White)
        }
        if (expanded) {
            datePickerDialog.show()
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddProjectScreenPreview() {
    // Mock ViewModel
    val mockViewModel = ProjetViewModel(sessionManager = SessionManager(LocalContext.current)).apply {
        // Optionally, add mock data here if needed
    }

    // Create a dummy NavController
    val navController = rememberNavController()

    // Call the AddProjectScreen with mock dependencies
    AddProjectScreen(
        navController = navController,
        sessionManager = SessionManager(LocalContext.current),
        onProjectAdded = {
            // Do nothing, this is just for preview purposes
        }
    )
}
