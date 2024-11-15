package tn.esprit.freelancy.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposeauthui.LoginViewModel


import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel
) {
    val email = viewModel.email.collectAsState()
    val password = viewModel.password.collectAsState()
    val loginSuccess = viewModel.loginSuccess.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        // Gradient background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF6A1B9A), Color(0xFF283593)) // Purple to Blue gradient
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Welcome Back!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 50.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Email TextField
                TextField(
                    value = email.value,
                    onValueChange = { viewModel.onEmailChange(it) },
                    label = { Text("Email", color = Color.White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.White,
                        cursorColor = Color.White,
                        containerColor = Color.Transparent, // Makes the background transparent
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password TextField
                TextField(
                    value = password.value,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text("Password", color = Color.White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.White,
                        cursorColor = Color.White,
                        containerColor = Color.Transparent, // Makes the background transparent
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    visualTransformation = PasswordVisualTransformation() // Hides the password
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Forgot Password Row
                Row(
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Forgot your password?",
                        color = Color.White.copy(alpha = 0.7f)

                    )
                    TextButton(onClick = { navController.navigate("forgot_password") }
                                ) {

                        Text(
                            text = "Reset Password",
                            color = Color.Black)}
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Login Button
                Button(
                    onClick = {
                        viewModel.login()
                        if (errorMessage.value != null) {
                            showErrorDialog = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF283593)), // Blue
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(5.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Log In", color = Color.White, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = {
                        navController.navigate("signup") // Naviguer vers la page d'inscription
                    }
                ) {
                    Text("Don't have an account? Sign up here", color = MaterialTheme.colorScheme.primary)
                }
                // Success Message
                if (loginSuccess.value) {
                    LaunchedEffect(Unit) {
                        navController.navigate("home/${email.value}") {
                            popUpTo("login") { inclusive = true } // Supprime l'écran de connexion de la pile
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f)) // Push content to the top

                // Error Dialog
                if (showErrorDialog && errorMessage.value != null) {
                    AlertDialog(
                        onDismissRequest = { showErrorDialog = false },
                        confirmButton = {
                            TextButton(onClick = { showErrorDialog = false }) {
                                Text("OK", color = MaterialTheme.colorScheme.primary)
                            }
                        },
                        title = { Text("Error") },
                        text = { Text(errorMessage.value!!) }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 320, heightDp = 640, apiLevel = 30)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController, viewModel())
}

