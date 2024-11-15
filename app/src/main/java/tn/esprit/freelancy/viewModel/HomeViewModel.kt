package tn.esprit.freelancy.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tn.esprit.freelancy.model.UserProfile
import tn.esprit.freelancy.model.GetUserIdRequest
import tn.esprit.freelancy.model.GetUserIdResponse
import tn.esprit.freelancy.remote.RetrofitClient

class HomeViewModel : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile

    private val _user = MutableStateFlow<GetUserIdResponse?>(null)
    val user: StateFlow<GetUserIdResponse?> = _user

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _updateSuccess = MutableStateFlow<Boolean?>(null)
    val updateSuccess: StateFlow<Boolean?> = _updateSuccess

    // Fetch user profile by username
    fun fetchUser(username: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authService.getUserId(GetUserIdRequest(username))
                _user.value = response
                _userProfile.value = UserProfile(
                    idUser = response.id,
                    username = response.username,
                    email = response.email,
                    avatarUrl = "https://i.pravatar.cc/150?img=3"
                )
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }
    fun deleteAccount(userId: String) {
        viewModelScope.launch {
            try {
                RetrofitClient.authService.deleteUser(userId) // Appel de l'API
                _userProfile.value = null // Réinitialise les données utilisateur après suppression
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }


    // Update user profile

}
