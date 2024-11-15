package tn.esprit.freelancy.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    viewModel: SignupViewModel,  // Le ViewModel passé en paramètre
    onSignupSuccess: () -> Unit // Une lambda appelée après succès de l'inscription
) {

    val username = viewModel.username.collectAsState() // État pour l'email


    val email = viewModel.email.collectAsState() // État pour l'email
    val password = viewModel.password.collectAsState() // État pour le mot de passe
    val signupSuccess = viewModel.signupSuccess.collectAsState() // Succès de l'inscription
    val errorMessage = viewModel.errorMessage.collectAsState() // Message d'erreur éventuel
    Surface(
        modifier = Modifier.fillMaxSize()
            .background(brush = Brush.linearGradient(listOf(Color(0xFF6A1B9A), Color(0xFF283593))) )

    ) {
        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.linearGradient(listOf(Color(0xFF6A1B9A), Color(0xFF283593))) )
                .padding(16.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Champ pour l'email
            OutlinedTextField(
                value = email.value,
                onValueChange = { viewModel.onEmailChange(it) }, // Appel à la fonction du ViewModel
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    containerColor = Color.Transparent, // Makes the background transparent
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )            )

            Spacer(modifier = Modifier.height(16.dp))

            // Champ pour le mot de passe
            OutlinedTextField(
                value = username.value,
                onValueChange = { viewModel.onUsernameChange(it) }, // Appel à la fonction du ViewModel
                label = { Text("username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    containerColor = Color.Transparent, // Makes the background transparent
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )            )
            Spacer(modifier = Modifier.height(16.dp))

            // Champ pour le mot de passe
            OutlinedTextField(
                value = password.value,
                onValueChange = { viewModel.onPasswordChange(it) }, // Appel à la fonction du ViewModel
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    containerColor = Color.Transparent, // Makes the background transparent
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bouton d'inscription
            Button(
                onClick = {
                    viewModel.signup(username.value, email.value, password.value)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }
            TextButton(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            ) {
                Text("Already have an account? Log in here", color = MaterialTheme.colorScheme.background)
            }
            // Affiche un message d'erreur s'il y en a un
            errorMessage.value?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

    }




    // Si l'inscription réussit, naviguez vers une autre page
    errorMessage.value?.let {
        Text(text = it, color = MaterialTheme.colorScheme.error)
    }

    // Naviguez en cas de succès
    if (signupSuccess.value) {
        LaunchedEffect(Unit) {
            onSignupSuccess()
        }
    }

}
@Preview(showBackground = true, widthDp = 320, heightDp = 640, apiLevel = 30)
@Composable
fun SignupScreenPreview() {
    val navController = rememberNavController()

    // Passer une fonction lambda vide pour onSignupSuccess
    SignupScreen(
        navController = navController,
        viewModel = viewModel(factory = SignupViewModelFactory(AuthRepository(RetrofitClient.authService))),
        onSignupSuccess = {}
    )
}



