package tn.esprit.freelancy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.remote.UserAPI
import tn.esprit.freelancy.repository.AuthRepository
import tn.esprit.freelancy.ui.theme.JetpackComposeAuthUITheme
import tn.esprit.freelancy.view.EditProfileScreen
import tn.esprit.freelancy.view.ForgotPasswordScreen
import tn.esprit.freelancy.view.HomeContent
import tn.esprit.freelancy.view.LoginScreen
import tn.esprit.freelancy.view.ProfileContent
import tn.esprit.freelancy.view.SignupScreen
import tn.esprit.freelancy.viewModel.ForgotPasswordViewModel
import tn.esprit.freelancy.viewModel.SignupViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeAuthUITheme {
                /// Let just add navigation so users can go from one screen to another
                NavigationView()
            }
        }
    }
}
@Composable
fun NavigationView() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController, viewModel())
        }
        composable("signup") {
            SignupScreen(
                navController = navController, // Passez le navController ici
                viewModel = viewModel(factory = SignupViewModelFactory(AuthRepository(RetrofitClient.authService))),
                onSignupSuccess = {
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }


        composable("home/{email}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("email") ?: ""
            HomeContent(navController, username, viewModel())
        }
        composable("edit_profile/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            EditProfileScreen(
                navController = navController,
                email = email,
                userRepository = AuthRepository(api = RetrofitClient.authService),
                onSaveSuccess = { /* Callback on success */ }
            )
        }
        composable("forgot_password") {
            val viewModel: ForgotPasswordViewModel = viewModel()
            ForgotPasswordScreen(viewModel)
        }



        composable("profile/{email}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("email") ?: ""
            ProfileContent(navController, username, viewModel())
        }
    }
}



