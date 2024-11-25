package tn.esprit.freelancy.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.repository.AuthRepository
import tn.esprit.freelancy.viewModel.SignupViewModel
import tn.esprit.freelancy.viewModel.SignupViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    navController: NavHostController,
) {
    val authRepository = AuthRepository(RetrofitClient.authService)
    val viewModel: SignupViewModel = viewModel(factory = SignupViewModelFactory(authRepository))

    val username = viewModel.username.collectAsState()
    val email = viewModel.email.collectAsState()
    val password = viewModel.password.collectAsState()
    val password2 = viewModel.password2.collectAsState()
    val signupSuccess = viewModel.signupSuccess.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF6A1B9A), Color(0xFF283593))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Section
            Text(
                text = "Register",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 32.dp)
            )

            // Form Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Full Name Field
                    OutlinedTextField(
                        value = username.value,
                        onValueChange = { viewModel.onUsernameChange(it) },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF6A1B9A),
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    // Email Field
                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { viewModel.onEmailChange(it) },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF6A1B9A),
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    // Password Field
                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { viewModel.onPasswordChange(it) },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF6A1B9A),
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    // Verify Password Field
                    OutlinedTextField(
                        value = password2.value,
                        onValueChange = { viewModel.onPasswordChange2(it) },
                        label = { Text("Verify Password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF6A1B9A),
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    // Error Message
                    errorMessage.value?.let { error ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = error,
                                color = Color.Red,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Signup Button
                    Button(
                        onClick = {
                            when {
                                username.value.isEmpty() || email.value.isEmpty() || password.value.isEmpty() -> {
                                    viewModel.onErrorMessage("Please fill in all fields")
                                }
                                password.value.length < 8 -> {
                                    viewModel.onErrorMessage("Password must be at least 8 characters long")
                                }
                                !android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches() -> {
                                    viewModel.onErrorMessage("Invalid email address")
                                }
                                password.value != password2.value -> {
                                    viewModel.onErrorMessage("Passwords do not match")
                                }
                                else -> {
                                    viewModel.signup(username.value, email.value, password.value)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF283593),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Sign Up")
                    }

                    // Login Navigation
                    TextButton(
                        onClick = {
                            navController.navigate("login") {
                                popUpTo("signup") { inclusive = true }
                            }
                        }
                    ) {
                        Text("Already have an account? Log in here", color = Color(0xFF6A1B9A))
                    }
                }
            }

            // Footer Section
            Spacer(modifier = Modifier.height(24.dp))
        }


    }

    // Navigate to Next Page on Success
    if (signupSuccess.value) {
        LaunchedEffect(Unit) {
            navController.navigate("role_selection/${username.value}") {
                popUpTo("Signup") { inclusive = true }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun SignupScreenPreview() {
    val navController = rememberNavController()
    SignupScreen(
        navController = navController,

    )
}
