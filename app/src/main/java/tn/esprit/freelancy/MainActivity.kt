package tn.esprit.freelancy

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.repository.AuthRepository
import tn.esprit.freelancy.repository.NotificationRepository
import tn.esprit.freelancy.session.PreferenceManager
import tn.esprit.freelancy.session.SessionManager
import tn.esprit.freelancy.ui.theme.JetpackComposeAuthUITheme
import tn.esprit.freelancy.view.CreateProfileScreen
import tn.esprit.freelancy.view.CvAnalysisScreen
import tn.esprit.freelancy.view.EditProfileScreen
import tn.esprit.freelancy.view.ForgotPasswordScreen
import tn.esprit.freelancy.view.Home
import tn.esprit.freelancy.view.HomeContent
import tn.esprit.freelancy.view.LoginScreen
import tn.esprit.freelancy.view.ProfileScreen
import tn.esprit.freelancy.view.RoleSelectionScreen
import tn.esprit.freelancy.view.SignupScreen
import tn.esprit.freelancy.view.SplashScreen
import tn.esprit.freelancy.view.projet.AddProjectScreen
import tn.esprit.freelancy.view.projet.EntrepreneurNotificationScreen
import tn.esprit.freelancy.view.projet.NotificationScreen
import tn.esprit.freelancy.view.projet.OngoingProjectsScreen
import tn.esprit.freelancy.view.projet.ProjectDetailScreen
import tn.esprit.freelancy.view.projet.ProjetListScreen
import tn.esprit.freelancy.viewModel.ForgotPasswordViewModel
import tn.esprit.freelancy.viewModel.HomeViewModel
import tn.esprit.freelancy.viewModel.HomeViewModelFactory
import tn.esprit.freelancy.viewModel.SignupViewModel
import tn.esprit.freelancy.viewModel.SignupViewModelFactory
import tn.esprit.freelancy.viewModel.projet.NotificationViewModel
import tn.esprit.freelancy.viewModel.projet.ProjetViewModel
import tn.esprit.freelancy.viewModel.projet.ProjetViewModelFactory

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
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NavigationView(preferenceManager: PreferenceManager) {
    val navController = rememberNavController()
    val authRepository = AuthRepository(RetrofitClient.authService) // Create the repository
    val repository = SessionManager(LocalContext.current)


    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController = navController, SessionManager(LocalContext.current))
        }
        composable("login") {

            LoginScreen(navController = navController,SessionManager(LocalContext.current))
        }
        composable("home/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(SessionManager(LocalContext.current))) // Use the factory
            HomeContent(navController, email, viewModel())
        }
        composable("homeC/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(SessionManager(LocalContext.current))) // Use the factory
            Home(navController, email, homeViewModel, SessionManager(LocalContext.current))
        }

        composable("role_selection/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val signupViewModel: SignupViewModel = viewModel(
                factory = SignupViewModelFactory(authRepository)
            )
            RoleSelectionScreen(navController = navController, onRoleSelected = { role ->
                signupViewModel.updateUserRole(role, username) // Update user role via ViewModel
                navController.navigate("update_profile/$username") }, username = username)
        }

        composable("update_profile/{username}") {backStackEntry ->
            val email = backStackEntry.arguments?.getString("username") ?: ""

            CreateProfileScreen(navController = navController, username = email)


        }
        composable("project_detail/{projectId}") { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
            val viewModel: ProjetViewModel = viewModel(
                factory = ProjetViewModelFactory(repository)
            )
            ProjectDetailScreen(navController = navController, projectId = projectId, viewModel =viewModel)
        }

        composable("signup") {
            SignupScreen(
                navController = navController,

            )
        }
        composable("alerts") {
            EntrepreneurNotificationScreen(entrepreneurId = "123"
                ,notificationViewModel = NotificationViewModel(NotificationRepository(RetrofitClient.projetApi),sessionManager = SessionManager(LocalContext.current)),navController
                )
        }
        composable("profile/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(SessionManager(LocalContext.current))) // Use the factory
            val userProfile by homeViewModel.userProfile2.collectAsState()
            val errorMessage by homeViewModel.errorMessage.collectAsState()


            
            LaunchedEffect(email) {
                homeViewModel.fetchUserProfile(email)
            }

            when {
                userProfile != null -> {
                    ProfileScreen(navController = navController, user = userProfile!!,SessionManager(LocalContext.current))
                }
                errorMessage != null -> {
                    Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
                }
                else -> {
                    Text(text = "Loading...")
                }
            }
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
        composable("cv_analysis") {
            CvAnalysisScreen(onBack = { navController.popBackStack() })
        }
        composable("projects") {
            val viewModel: ProjetViewModel = viewModel(
                factory = ProjetViewModelFactory(repository)
            )
            ProjetListScreen(
                navController = navController,
                viewModel = viewModel,
                sessionManager = SessionManager(LocalContext.current),
                onAddProject = { navController.navigate("addProject") }
            )
        }
        composable("addProject") {
            AddProjectScreen(navController= navController, sessionManager = SessionManager(LocalContext.current), onProjectAdded = { navController.popBackStack() })
        }
        composable("ongoing_projects") {
            val projectViewModel: ProjetViewModel = viewModel(
                factory = ProjetViewModelFactory(repository)
            )
            OngoingProjectsScreen(navController = navController, viewModel = projectViewModel, sessionManager = SessionManager(LocalContext.current))
        }


    }

}




