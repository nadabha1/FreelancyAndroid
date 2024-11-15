package tn.esprit.freelancy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tn.esprit.freelancy.model.GetUserIdRequest
import tn.esprit.freelancy.model.GetUserIdResponse
import tn.esprit.freelancy.model.UserProfile
import tn.esprit.freelancy.model.UserProfile1
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.repository.AuthRepository

class UpdateProfileViewModel(private val userRepository: AuthRepository) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile

    private val _updateSuccess = MutableStateFlow<Boolean?>(null)
    val updateSuccess: StateFlow<Boolean?> = _updateSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _user = MutableStateFlow<GetUserIdResponse?>(null)
    val user: StateFlow<GetUserIdResponse?> = _user

    // Fonction pour définir un message d'erreur
    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }

    // Fonction pour récupérer le profil utilisateur à partir du backend
    fun fetchUser(username: String) {
        viewModelScope.launch {
            _loading.value = true // Démarrage du chargement
            try {
                val response = RetrofitClient.authService.getUserId(GetUserIdRequest(username))
                if (response.id.isNotEmpty()) {
                    // Mettre à jour les données de l'utilisateur
                    _user.value = response
                    _userProfile.value = UserProfile(
                        idUser = response.id,
                        username = response.username,
                        email = response.email,
                        avatarUrl = "https://i.pravatar.cc/150?img=3" // Avatar par défaut
                    )
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "User not found"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching user: ${e.message}"
            } finally {
                _loading.value = false // Fin du chargement
            }
        }
    }

    // Fonction pour mettre à jour le profil utilisateur
    fun updateUserProfile(updatedProfile: UserProfile1, userProfile: UserProfile) {
        viewModelScope.launch {
            _loading.value = true // Démarrage du chargement
            try {
                val response = RetrofitClient.authService.updateUserProfile(updatedProfile)
                if (response.username.isNotEmpty()) {
                    userProfile.avatarUrl ="15154"
                    // Mettre à jour localement les données utilisateur
                    _userProfile.value = userProfile
                    _updateSuccess.value = true
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Failed to update profile"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error updating profile: ${e.message}"
            } finally {
                _loading.value = false // Fin du chargement
            }
        }
    }
}
