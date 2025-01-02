package tn.esprit.freelancy.session
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import tn.esprit.freelancy.model.chat.AppState
import tn.esprit.freelancy.model.chat.SignInResult
import tn.esprit.freelancy.model.chat.SignInResult2
import tn.esprit.freelancy.model.user.GetUserIdResponse
import tn.esprit.freelancy.model.user.LoginRequest
import tn.esprit.freelancy.model.user.UserProfileComplet
import tn.esprit.freelancy.model.user.UserProfileFireBase
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.repository.CvRepository
import tn.esprit.freelancy.viewModel.CvViewModel

class SessionManager(context: Context) {
    val userRole: StateFlow<String?> = flow {
        emit(sharedPreferences.getString(KEY_ROLE, null))
    }.stateIn(CoroutineScope(Dispatchers.IO), SharingStarted.Lazily, null)
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val isLoggedIn: StateFlow<Boolean> = flow {
        emit(sharedPreferences.getString(KEY_AUTH_TOKEN, null) != null)
    }.stateIn(CoroutineScope(Dispatchers.IO), SharingStarted.Lazily, false)

    val userEmail: StateFlow<String?> = flow {
        emit(sharedPreferences.getString(KEY_EMAIL, null))
    }.stateIn(CoroutineScope(Dispatchers.IO), SharingStarted.Lazily, null)
    companion object {
        private const val PREF_NAME = "UserSession"
        private const val KEY_EMAIL = "email"
        private const val KEY_ROLE= "role"
        private const val KEY_ID = "id" // Add key for user ID
        private const val KEYFIRE_ID = "idFire" // Add key for user ID
        private const val KEY_PASSWORD = "password" // Avoid storing passwords unless encrypted
        private const val KEY_AUTH_TOKEN = "auth_token"
    }

    // Save user session (e.g., email and authentication token)
    fun saveSession(id: String,email: String, authToken: String,role:String,firebaseId:String) {
        sharedPreferences.edit().apply {
            putString(KEY_ID, id) // Save the user ID
            putString(KEY_EMAIL, email)
            putString(KEY_AUTH_TOKEN, authToken)
            putString(KEY_ROLE, role)
            putString(KEYFIRE_ID, firebaseId)
            apply()
        }
    }

    // Retrieve user session
    fun getSession(): SessionData? {
        val id = sharedPreferences.getString(KEY_ID, null)
        val idFirebase = sharedPreferences.getString(KEYFIRE_ID, null)
        println("User ID: $id")
        val email = sharedPreferences.getString(KEY_EMAIL, null)
        val authToken = sharedPreferences.getString(KEY_AUTH_TOKEN, null)
        val role = sharedPreferences.getString(KEY_ROLE, null)
        return if (id != null &&email != null && authToken != null && role != null && idFirebase != null)
        {
            SessionData(id,email, authToken,role,idFirebase)
        } else {
            null
        }
    }
    private val _state= MutableStateFlow(AppState())
    val state=_state.asStateFlow()
    fun inSigneInResult(signInResult: SignInResult2) {
        _state.update {
            it.copy(
                userProfil = signInResult.data?.let { user ->
                    UserProfileFireBase(
                        userId = user.userId ?: "",
                        username = user.username ?: "",
                        email = user.email ?: "",
                        skills = user.skills ?: "",
                        role = user.role ?: ""
                    )
                }
            )
        }
    }
    // Clear session (e.g., on logout)
    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }
    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_ID, null)
    }
    fun getUserFireBaseId(): String? {
        return sharedPreferences.getString(KEYFIRE_ID, null)
    }
    val cvRepository: CvRepository = CvRepository(RetrofitClient.cvApiService)

    // Authenticate the user
    suspend fun authenticate(email: String, password: String,firebaseId: String): Boolean {
        return try {
            // 1. Call the API with the user's email and password
            val response = RetrofitClient.authService.login2(LoginRequest(email, password))
            val responseBody = RetrofitClient.authService.getUserProfile(email)
            val id = responseBody.idUser
            println("User ID in session : $id")
            println("User password in session : $password")
            val roleName = RetrofitClient.authService.getRoleName(
                GetUserIdResponse(id)
            )
            println("Role fetched in session successfully with name: ${roleName.idRole}")
            CvViewModel(repository = cvRepository).adduserToFirestore(UserProfileComplet(
                idUser = id,
                username = responseBody.username,
                email = responseBody.email,
                role = roleName.idRole,
                skills = responseBody.skills
            ))
            inSigneInResult(SignInResult2(
                data = responseBody?.skills?.let {
                    UserProfileFireBase(
                        userId = id,
                        username = responseBody.username,
                        email = responseBody.email,
                        skills = it,
                        role = roleName.idRole
                    )
                },
                errorMessage = null
            ))
            saveSession(id,email, response.access_token,roleName.idRole,firebaseId)

            // 3. Return true to indicate a successful login
            true
        } catch (e: retrofit2.HttpException) {
            // Handle HTTP errors (e.g., wrong credentials, server errors)
            when (e.code()) {
                401 -> {
                    println("Invalid credentials: ${e.message}")
                }
                else -> {
                    println("Server error: ${e.message}")
                }
            }
            false // Return false to indicate a failed login
        } catch (e: Exception) {
            // Handle other errors (e.g., no internet, timeout)
            println("Failed to connect to server: ${e.message}")
            false // Return false for failure
        }
    }



}

// Data class to hold session information
data class SessionData(
    val id: String,
    val email: String,
    val authToken: String,
    val role: String,
    val firebaseId: String
)
