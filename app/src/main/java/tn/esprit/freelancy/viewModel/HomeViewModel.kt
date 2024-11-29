package tn.esprit.freelancy.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tn.esprit.freelancy.model.user.GetRoleIdResponse
import tn.esprit.freelancy.model.user.GetUserIdResponse
import tn.esprit.freelancy.model.user.GetUserResponse
import tn.esprit.freelancy.model.user.GetUserResponsetest
import tn.esprit.freelancy.model.user.UserProfileComplet
import tn.esprit.freelancy.remote.RetrofitClient
class HomeViewModel : ViewModel() {

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
}
