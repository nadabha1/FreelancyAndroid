package tn.esprit.freelancy.viewModel
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tn.esprit.freelancy.model.projet.Project
import tn.esprit.freelancy.model.projet.Projet
import tn.esprit.freelancy.model.user.GetRoleIdResponse
import tn.esprit.freelancy.model.user.GetUserIdResponse
import tn.esprit.freelancy.model.user.GetUserResponse
import tn.esprit.freelancy.model.user.GetUserResponsetest
import tn.esprit.freelancy.model.user.UserProfileComplet
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.session.SessionManager

class HomeViewModel(private val sessionManager: SessionManager)  : ViewModel() {

    val _projects = MutableStateFlow<List<Projet>>(emptyList())
    val projects: StateFlow<List<Projet>> = _projects
    val _projectsIa = MutableStateFlow<List<Project>>(emptyList())
    val projectsIa: StateFlow<List<Project>> = _projectsIa

    val userId: String? = sessionManager.getUserId()


    private val _userProfile = MutableStateFlow<GetUserResponse?>(null)
    val userProfile: StateFlow<GetUserResponse?> = _userProfile

    private val _userProfile2 = MutableStateFlow<UserProfileComplet?>(null)
    val userProfile2: StateFlow<UserProfileComplet?> = _userProfile2

    private val _user = MutableStateFlow<GetUserResponsetest?>(null)
    val user: StateFlow<GetUserResponsetest?> = _user

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    private val _role = MutableStateFlow<GetRoleIdResponse?>(null)
    val role: StateFlow<GetRoleIdResponse?> = _role

    private val _updateSuccess = MutableStateFlow<Boolean?>(null)
    val updateSuccess: StateFlow<Boolean?> = _updateSuccess

    val userProfilel = MutableStateFlow<UserProfileComplet?>(null)

    // Fetch user profile by email
    fun fetchUserProfile(email: String) {
        viewModelScope.launch {
            try {
                println("Fetching user profile for email: $email") // Debug log
                val user = RetrofitClient.authService.getUserProfile(email)
                println("User fetched: $user") // Debug log
                _userProfile2.value = user // Assign the user to StateFlow

                println("User profile fetched successfully") // Debug log
                val roleName = RetrofitClient.authService.getRoleName(GetUserIdResponse(user.idUser))
                _role.value = roleName
                println("Role fetched successfully with name: ${_role.value?.idRole}") // Debug log

            } catch (e: Exception) {
                println("Error fetching user profile: ${e.message}") // Debug log
                _userProfile2.value = null
            }
        }
    }


    // API call to fetch user profile
    private suspend fun fetchUserProfileFromApi(email: String): UserProfileComplet? {
        return try {
            val response = RetrofitClient.authService.getUserProfile(email) // API call
            if (response.idUser.isNotEmpty()) {
                response // Return the response body
            } else {
                println("API Error: ${response}")
                null
            }
        } catch (e: Exception) {
            println("Error fetching user profile: ${e.message}")
            null
        }
    }

    fun deleteAccount(userId: String) {
        viewModelScope.launch {
            try {
                RetrofitClient.authService.deleteUser(userId) // API call
                _userProfile.value = null // Reset user data after deletion
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    init {
        loadProjectsIA()
    }
    private fun loadProjectsIA() {
        // Fetch projects from your backend using Retrofit or another data source
        viewModelScope.launch {
            if (userId != null) {
                val projectList = RetrofitClient.projetApi.getOngoingProjects(userId)
                _projectsIa.value = projectList
            }

        }
    }

    fun fetchProjectByIa() {
        viewModelScope.launch {
            try {
                if (userId != null) {

                    val response = RetrofitClient.projetApi.getOngoingProjects(userId)
                    _projectsIa.value = response

                }
                if (userId == null) {
                    Log.e("ProjetViewModel", "User ID is null or undefined")
                }
            } catch (e: Exception) {
                println("Error fetching projects: ${e.message}")
            }
        }
    }
}
