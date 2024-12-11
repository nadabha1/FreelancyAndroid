package tn.esprit.freelancy.view
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import tn.esprit.freelancy.R
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.repository.AuthRepository
import tn.esprit.freelancy.viewModel.UpdateProfileViewModel
import tn.esprit.freelancy.viewModel.UpdateProfileViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(
    navController: NavController,
    username: String
) {
    var dateOfBirth by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("Tunisia") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var showPopup by remember { mutableStateOf(false) } // State for popup visibility

    val userRepository: AuthRepository = AuthRepository(RetrofitClient.authService)
    val viewModel: UpdateProfileViewModel = viewModel(factory = UpdateProfileViewModelFactory(userRepository))
    val updateSuccess by viewModel.updateSuccess.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> profileImageUri = uri }
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
                text = "Create Your Profile",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
            )

            // Profile Picture Section
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                if (profileImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = profileImageUri),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(60.dp))
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.profile_placeholder),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(60.dp))
                            .background(Color.Gray)
                    )
                }

                IconButton(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFF1E88E5), RoundedCornerShape(16.dp))
                        .align(Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Upload Icon",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                label = { Text("Date of Birth *") },
                placeholder = { Text("yyyy-mm-dd") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF1E88E5),
                    unfocusedBorderColor = Color.Gray,
                    textColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                label = { Text("Country *") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF1E88E5),
                    unfocusedBorderColor = Color.Gray,
                    textColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.updateUserProfile(
                        username = username,
                        dateOfBirth = dateOfBirth.ifEmpty { "2000-01-01" },
                        country = country.ifEmpty { "Unknown" },
                        profilePictureUrl = profileImageUri?.toString() ?: ""
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Create", color = Color.White)
            }
        }
    }

    if (updateSuccess) {
        LaunchedEffect(Unit) {
            showPopup = true
        }
    }

    if (showPopup) {
        SkillSelectionPopup(
            onManualEntry = {
                showPopup = false
                navController.navigate("manual_skills_entry/$username")
            },
            onUploadCV = {
                showPopup = false
                navController.navigate("cv_upload/$username")
            }
        )
    }
}
@Composable
fun SkillSelectionPopup(
    onManualEntry: () -> Unit,
    onUploadCV: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = "How would you like to tell us about yourself?") },
        text = {
            Text(
                text = "We need to get a sense of your education, experience, and skills. You can edit it before your profile goes live."
            )
        },
        confirmButton = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = onUploadCV,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Upload Your Resume")
                }
                Button(
                    onClick = onManualEntry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Fill Out Manually (15 min)")
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    )
}


@Preview(showBackground = true)
@Composable
fun CreateProfilePreview() {
    CreateProfileScreen(
        navController = rememberNavController(),
        username = "Nada"
    )
}
