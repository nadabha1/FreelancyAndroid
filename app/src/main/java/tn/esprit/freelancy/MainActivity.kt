package tn.esprit.freelancy

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import tn.esprit.freelancy.googleSign.GoogleAuthUi
import tn.esprit.freelancy.model.chat.AppState
import tn.esprit.freelancy.model.chat.ChatUserData
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.repository.AuthRepository
import tn.esprit.freelancy.repository.CvRepository
import tn.esprit.freelancy.repository.NotificationRepository
import tn.esprit.freelancy.session.PreferenceManager
import tn.esprit.freelancy.session.SessionManager
import tn.esprit.freelancy.ui.theme.JetpackComposeAuthUITheme
import tn.esprit.freelancy.view.CreateProfileScreen
import tn.esprit.freelancy.view.CvAnalysisScreen
import tn.esprit.freelancy.view.CvUploadScreen
import tn.esprit.freelancy.view.EditProfileScreen
import tn.esprit.freelancy.view.ForgotPasswordScreen
import tn.esprit.freelancy.view.Home
import tn.esprit.freelancy.view.LoginScreen
import tn.esprit.freelancy.view.ManualSkillsEntryScreen
import tn.esprit.freelancy.view.ProfileScreen
import tn.esprit.freelancy.view.RoleSelectionScreen
import tn.esprit.freelancy.view.SignupScreen
import tn.esprit.freelancy.view.SplashScreen
import tn.esprit.freelancy.view.chat.Chat
import tn.esprit.freelancy.view.chat.ChatScreen
import tn.esprit.freelancy.view.projet.AddProjectScreen
import tn.esprit.freelancy.view.projet.EntrepreneurNotificationScreen
import tn.esprit.freelancy.view.projet.NotificationScreen
import tn.esprit.freelancy.view.projet.OngoingProjectsScreen
import tn.esprit.freelancy.view.projet.ProjectDetailScreen
import tn.esprit.freelancy.view.projet.ProjectDetailScreenForEntrepreneur
import tn.esprit.freelancy.view.projet.ProjetListScreen
import tn.esprit.freelancy.viewModel.CvViewModel
import tn.esprit.freelancy.viewModel.CvViewModelFactory
import tn.esprit.freelancy.viewModel.ForgotPasswordViewModel
import tn.esprit.freelancy.viewModel.HomeViewModel
import tn.esprit.freelancy.viewModel.HomeViewModelFactory
import tn.esprit.freelancy.viewModel.SignupViewModel
import tn.esprit.freelancy.viewModel.SignupViewModelFactory
import tn.esprit.freelancy.viewModel.chat.ChatViewModel
import tn.esprit.freelancy.viewModel.chat.ChatViewModelFactory
import tn.esprit.freelancy.viewModel.projet.NotificationViewModel
import tn.esprit.freelancy.viewModel.projet.ProjetViewModel
import tn.esprit.freelancy.viewModel.projet.ProjetViewModelFactory

class MainActivity : ComponentActivity() {
    private val dataStore by preferencesDataStore(name = "user_preferences")
    private val preferenceManager by lazy { PreferenceManager(this) }
    private val sessionManager by lazy { SessionManager(applicationContext) }
    private val chatViewModelFactory by lazy { ChatViewModelFactory(sessionManager) }
    private val chatViewModel: ChatViewModel by viewModels { chatViewModelFactory }
     val googleAuthUiClient by lazy {
        GoogleAuthUi(
            context =  applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext),
            viewModel = chatViewModel
        )


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeAuthUITheme {
                /// Let just add navigation so users can go from one screen to another
                NavigationView(preferenceManager,googleAuthUiClient,sessionManager)
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NavigationView(preferenceManager: PreferenceManager,googleAuthUiClient: GoogleAuthUi,sessionManager: SessionManager) {
    val navController = rememberNavController()
    val authRepository = AuthRepository(RetrofitClient.authService) // Create the repository
    val repository = SessionManager(LocalContext.current)
    val chatViewModelFactory = remember { ChatViewModelFactory(sessionManager) }
    val chatViewModel: ChatViewModel = viewModel(factory = chatViewModelFactory)


    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController = navController, SessionManager(LocalContext.current))
        }
        composable("login") {

            LoginScreen(navController = navController,SessionManager(LocalContext.current))
        }
        composable("cv_upload/{username}") {backStackEntry ->
            val email = backStackEntry.arguments?.getString("username") ?: ""
            val cvRepository: CvRepository = CvRepository(RetrofitClient.cvApiService)
            val homeViewModel: CvViewModel = viewModel(factory = CvViewModelFactory(cvRepository)) // Use the factory%
            LaunchedEffect(email) {
                homeViewModel.fetchUser(email)
            }
            CvUploadScreen(homeViewModel,navController, email)
        }
        composable("homeC/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(SessionManager(LocalContext.current))) // Use the factory
            val userData = googleAuthUiClient.getSingInUser()
            LaunchedEffect(Unit) {
                homeViewModel.fetchProjectByIa()
            }
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
        composable("project_detailEnt/{projectId}") { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
            val viewModel: ProjetViewModel = viewModel(
                factory = ProjetViewModelFactory(repository)
            )

            ProjectDetailScreenForEntrepreneur(navController = navController, projectId = projectId, viewModel =viewModel)


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
        composable("manual_skills_entry/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val cvRepository: CvRepository = CvRepository(RetrofitClient.cvApiService)
            val homeViewModel: CvViewModel = viewModel(factory = CvViewModelFactory(cvRepository)) // Use the factory%
            LaunchedEffect(username) {
                homeViewModel.fetchUser(username)
            }
            ManualSkillsEntryScreen(homeViewModel,navController, username)
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
        composable("chat") {
            val userId = SessionManager(LocalContext.current).getUserId()
            ChatScreen(
                viewModel = chatViewModel,
                showSingleChat = { usr, id ->
                    chatViewModel.getTp(id)
                    chatViewModel.setChatUser(usr, id)
                    navController.navigate("ChatMess/$id/${usr.userId}") },
                navController
            )
        }
        composable("ChatMess/{chatId}/{userId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")
            val userId = backStackEntry.arguments?.getString("userId")
            println("Navigating to ChatMess with chatId=$chatId, userId=$userId")

            // Gestion des cas nuls
            if (chatId.isNullOrEmpty() || userId.isNullOrEmpty()) {
                println("Erreur : chatId ou userId manquant")
                return@composable
            }


            val tp = chatViewModel.tp // Vérifiez si tp est mis à jour
            println("Données tp : $tp")
            var userChatData by remember { mutableStateOf<ChatUserData?>(null) }

            // Récupérer les données utilisateur
            LaunchedEffect(userId) {
                println("Fetching ChatUserData for userId=$userId")
                chatViewModel.fetchChatUserData(userId) { userData ->
                    userChatData = userData
                }

            }
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(SessionManager(LocalContext.current))) // Use the factory
            val userProfile by homeViewModel.userProfile2.collectAsState()

            val user = SessionManager(LocalContext.current).getSession()
            if (user != null) {
                LaunchedEffect(user.email) {
                    println("Email: ${user.email}")
                    homeViewModel.fetchUserProfile(user.email)
                }
            }

            // Vérification avant d'appeler le composable Chat
            if (tp != null &&userProfile != null && userChatData != null)  {
                Chat(
                    viewModel = chatViewModel,
                    userProfile = userProfile!!,
                    navController = navController,
                    userData = userChatData!!, // Passez les données utilisateur complètes ici
                    chatId = chatId
                )
            } else {
                println("Erreur : userProfile est null")
            }
        }

        composable("ongoing_projects") {
            val projectViewModel: ProjetViewModel = viewModel(
                factory = ProjetViewModelFactory(repository)
            )
            OngoingProjectsScreen(navController = navController, viewModel = projectViewModel, sessionManager = SessionManager(LocalContext.current))
        }


    }

}




