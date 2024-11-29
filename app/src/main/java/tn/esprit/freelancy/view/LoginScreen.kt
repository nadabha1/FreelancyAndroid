package tn.esprit.freelancy.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.res.painterResource

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import tn.esprit.freelancy.model.user.GetUserIdResponse
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.session.SessionManager
import tn.esprit.freelancy.viewModel.LoginViewModel
import tn.esprit.freelancy.viewModel.LoginViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    sessionManager: SessionManager,
) {
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(sessionManager))
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val loginFre by viewModel.loginFre.collectAsState()
    val loginEntr by viewModel.loginEntrep.collectAsState()

    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isLoading by viewModel.isLoading.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val role by viewModel.role.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF283593), // Top gradient color
                        Color(0xFF283593)  // Bottom gradient color
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section
            Text(
                text = "FreeLancy",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                textAlign = TextAlign.Center
            )

            // Login Form
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Login",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email Input
                    OutlinedTextField(
                        value = email,
                        onValueChange = viewModel::onEmailChange,
                        label = { Text("Email") },
                        isError = email.isEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    if (email.isEmpty()) {
                        Text(
                            text = "Email cannot be empty",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Input
                    OutlinedTextField(
                        value = password,
                        onValueChange = viewModel::onPasswordChange,
                        label = { Text("Password") },
                        isError = password.isEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    if (password.isEmpty()) {
                        Text(
                            text = "Password cannot be empty",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Forgot Password Link
                    Text(
                        text = "Forgot Password?",
                        fontSize = 14.sp,
                        color = Color(0xFF6A1B9A),
                        modifier = Modifier
                            .align(Alignment.Start)
                            .clickable {
                                navController.navigate("forgot_password")
                            }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Login Button
                    Button(
                        onClick = {
                            if (email.isNotEmpty() && password.isNotEmpty()) {
                                coroutineScope.launch {
                                    viewModel.login()
                                    val userRole = role?.idRole
                                    println("Email in LoginScreen loginnnnnnnnnnnnnn1111111: $userRole")

                                    println("Email in LoginScreen loginnnnnnnnnnnnnn: ${role?.idRole}")

                                }
                            } else {
                                errorMessage = "Please fill in all fields"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF283593)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Login", color = Color.White, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isLoading) {
                        CircularProgressIndicator(color = Color(0xFF283593))
                    }
                }
            }

            // Register Link
            TextButton(
                onClick = { navController.navigate("signup") }
            ) {
                Text("Don't have an account? Sign up here", color = MaterialTheme.colorScheme.primary)
            }
        }

        // Handle login success
        if (loginSuccess) {

            LaunchedEffect(Unit) {
                    if (loginEntr) {
                        navController.navigate("projects") {
                            println("Email in SplashScreen: $email")

                            // Clear the back stack so the user can't return to the splash screen
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                    else if (loginFre) {
                        navController.navigate("homeC/$email") {
                            println("Email in SplashScreen: $email")
                        }
                    }
            }
        }

        // Error Dialog
        errorMessage?.let { message ->
            AlertDialog(
                onDismissRequest = { errorMessage = null },
                confirmButton = {
                    TextButton(onClick = { errorMessage = null }) {
                        Text("OK", color = MaterialTheme.colorScheme.primary)
                    }
                },
                title = { Text("Error") },
                text = { Text(message) }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController = navController, SessionManager(LocalContext.current))
}
