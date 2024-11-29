package tn.esprit.freelancy.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tn.esprit.freelancy.repository.AuthRepository

class SignupViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username


    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    private val _password2 = MutableStateFlow("")
    val password2: StateFlow<String> = _password2
    private val _updatedSuccess = MutableStateFlow(false)
    val updatedSuccess: StateFlow<Boolean> = _updatedSuccess

    private val _signupSuccess = MutableStateFlow(false)
    val signupSuccess: StateFlow<Boolean> = _signupSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Fonction pour mettre à jour l'email
    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }
    fun onUsernameChange(newPassword: String) {
        _username.value = newPassword
    }

    // Fonction pour mettre à jour le mot de passe
    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }


    fun onPasswordChange2(newPassword: String) {
        _password2.value = newPassword
    }



    fun signup(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.signup(username, email, "Freelancer",password)
                println("Signup successful: $response")
                _signupSuccess.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _signupSuccess.value = false
            }
        }
    }

    fun onErrorMessage(s: String) {
        _errorMessage.value = s
    }

    fun updateUserRole(role: String, userId: String) {
        viewModelScope.launch {
            try {
                println("Updating role for user with userId: $userId")
                println("Updating role for user with role: $role")
                val response = authRepository.updateRole(role, userId)
                if (response.isSuccessful) {
                    _updatedSuccess.value = true
                    println("Role updated successfully")
                } else {
                    _updatedSuccess.value = false
                    println("Failed to update role: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _updatedSuccess.value = false
                println("Error while updating role: ${e.message}")
            }
        }
    }



}

