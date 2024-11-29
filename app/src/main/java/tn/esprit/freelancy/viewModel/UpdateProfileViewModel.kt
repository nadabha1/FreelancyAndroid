package tn.esprit.freelancy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tn.esprit.freelancy.model.user.GetUserIdRequest
import tn.esprit.freelancy.model.user.GetUserResponsetest
import tn.esprit.freelancy.model.user.UserProfile
import tn.esprit.freelancy.model.user.UserProfile1
import tn.esprit.freelancy.model.user.UserProfileUpdateRequest
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.repository.AuthRepository

class UpdateProfileViewModel(private val userRepository: AuthRepository) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _user = MutableStateFlow<GetUserResponsetest?>(null)
    val user: StateFlow<GetUserResponsetest?> = _user

    // Fonction pour définir un message d'erreur
    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }
    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> = _updateSuccess
    // Fonction pour récupérer le profil utilisateur à partir du backend
    fun fetchUser(username: String) {
        viewModelScope.launch {
            _loading.value = true // Démarrage du chargement
            try {
                val response = RetrofitClient.authService.getUserId(GetUserIdRequest(username))
                if (response.user.email.isNotEmpty()) {
                    // Mettre à jour les données de l'utilisateur
                    _user.value = response
                    _userProfile.value = UserProfile(
                        idUser = response.user.idUser!!,
                        username = response.user    .username,
                        email = response.user.email,
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

    fun updateUserProfile(username: String,dateOfBirth: String, country: String, profilePictureUrl: String) {
        viewModelScope.launch {
            try {

                val response = RetrofitClient.authService.createUserProfile(
                    UserProfileUpdateRequest(username, dateOfBirth, country, profilePictureUrl)
                )
                if (response.isSuccessful) {
                        println("user profile updated successfully")
                    _updateSuccess.value = true
                } else {
                    _updateSuccess.value = false
                    println("user profile update failed")

                }
            } catch (e: Exception) {
                println("user profile update failed")
                _updateSuccess.value = false
                e.printStackTrace()
            }
        }


    }



}
