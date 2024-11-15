package tn.esprit.freelancy.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tn.esprit.freelancy.repository.AuthRepository

class SignupViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

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

    // Fonction pour gérer l'inscription

    fun signup(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.signup(username, email, password)
                // Vous pouvez stocker l'utilisateur ou son ID ici si nécessaire
                println("Signup successful: $response")
                _signupSuccess.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _signupSuccess.value = false
            }
        }
    }
}

