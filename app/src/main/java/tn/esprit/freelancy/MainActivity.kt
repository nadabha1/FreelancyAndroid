package tn.esprit.freelancy

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tn.esprit.freelancy.model.SignupData
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.remote.UserAPI
import tn.esprit.freelancy.repository.AuthRepository
import tn.esprit.freelancy.session.PreferenceManager
import tn.esprit.freelancy.session.SessionManager
import tn.esprit.freelancy.ui.theme.JetpackComposeAuthUITheme
import tn.esprit.freelancy.view.EditProfileScreen
import tn.esprit.freelancy.view.ForgotPasswordScreen
import tn.esprit.freelancy.view.HomeContent
import tn.esprit.freelancy.view.LoginScreen
import tn.esprit.freelancy.view.ProfileContent
import tn.esprit.freelancy.view.SignupScreen
import tn.esprit.freelancy.view.SplashScreen
import tn.esprit.freelancy.viewModel.ForgotPasswordViewModel
import tn.esprit.freelancy.viewModel.LoginViewModel
import tn.esprit.freelancy.viewModel.LoginViewModelFactory
import tn.esprit.freelancy.viewModel.SignupViewModel

class MainActivity : ComponentActivity() {
    private val dataStore by preferencesDataStore(name = "user_preferences")
    private val preferenceManager by lazy { PreferenceManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeAuthUITheme {
                /// Let just add navigation so users can go from one screen to another
                NavigationView(preferenceManager)
            }
        }
    }
}@Composable
fun NavigationView(preferenceManager: PreferenceManager) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController = navController, SessionManager(LocalContext.current))
        }
        composable("login") {

            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(SessionManager(LocalContext.current))
            )

            LoginScreen(navController = navController,SessionManager(LocalContext.current))
        }
        composable("home/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            HomeContent(navController, email, viewModel())
        }
        composable("signup") {
            SignupScreen(
                navController = navController,
                onSignupSuccess = {
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }
        composable("profile/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ProfileContent(navController = navController, email = email, viewModel = viewModel())
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
    }
}
