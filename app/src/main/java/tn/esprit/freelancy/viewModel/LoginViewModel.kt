package com.example.jetpackcomposeauthui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tn.esprit.freelancy.model.LoginRequest
import tn.esprit.freelancy.remote.RetrofitClient

class LoginViewModel : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun login() {
        viewModelScope.launch {
            try {
                // Envoyer la requête de connexion
                val response = RetrofitClient.authService.login2(LoginRequest(_email.value, _password.value))

                // Vérifiez si le jeton est présent dans la réponse
                if (response.access_token.isNotEmpty()) {
                    _loginSuccess.value = true
                    _errorMessage.value = null // Réinitialisez le message d'erreur
                } else {
                    _loginSuccess.value = false
                    _errorMessage.value = "Login failed: No access token received"
                }
            } catch (e: retrofit2.HttpException) {
                // Gérer les erreurs HTTP spécifiques (exemple : 401 Unauthorized)
                when (e.code()) {
                    401 -> _errorMessage.value = "Invalid credentials: Please check your email and password."
                    403 -> _errorMessage.value = "Access denied: You do not have permission to log in."
                    else -> _errorMessage.value = "Server error: ${e.message()}"
                }
                _loginSuccess.value = false
            } catch (e: Exception) {
                // Gérer les autres exceptions (exemple : erreurs réseau)
                _errorMessage.value = "Failed to connect to server: ${e.message}"
                _loginSuccess.value = false
            }
        }
    }

}
