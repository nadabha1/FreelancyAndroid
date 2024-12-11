package tn.esprit.freelancy.viewModel.projet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tn.esprit.freelancy.model.user.UserProfileComplet
import tn.esprit.freelancy.remote.RetrofitClient
import java.util.Date

class AddProjectViewModel : ViewModel() {
    var projectTitle by mutableStateOf("")
    var projectDescription by mutableStateOf("")
    var projectTechnologies by mutableStateOf("")
    var projectBudget by mutableStateOf("")
    var projectDeadline by mutableStateOf(Date())
    var projectStatus by mutableStateOf("")
    var showDatePicker by mutableStateOf(false)
    var statusMenuExpanded by mutableStateOf(false)
    var showError by mutableStateOf(false)
    var errorMessageVal by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    private val _user = MutableStateFlow<UserProfileComplet?>(null)
    val user: StateFlow<UserProfileComplet?> = _user
    private val _userProfile = MutableStateFlow<UserProfileComplet?>(null)
    val userProfile: StateFlow<UserProfileComplet?> = _userProfile
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    private val _updateSuccess = MutableStateFlow<Boolean?>(null)
    val updateSuccess: StateFlow<Boolean?> = _updateSuccess

    fun fetchUser(email: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authService.getUserProfile(email)
                _user.value = response

                println("l'id est : "+response.idUser)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }
}