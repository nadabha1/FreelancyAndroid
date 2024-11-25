package tn.esprit.freelancy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tn.esprit.freelancy.model.LoginRequest
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.session.PreferenceManager
import tn.esprit.freelancy.session.SessionManager

class LoginViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun login() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.authService.login2(LoginRequest(_email.value, _password.value))
                 println(response.access_token)
                if (response.access_token.isNotEmpty()) {
                    _loginSuccess.value = true
                    _errorMessage.value = null
                    sessionManager.saveAccessToken(email = _email.value, token = response.access_token)
                } else {
                    _loginSuccess.value = false
                    _errorMessage.value = "Login failed: No access token received"
                }
            } catch (e: retrofit2.HttpException) {
                _errorMessage.value = when (e.code()) {
                    401 -> "Invalid credentials: Please check your email and password."
                    403 -> "Access denied: You do not have permission to log in."
                    else -> "Server error: ${e.message()}"
                }
                _loginSuccess.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Failed to connect to server: ${e.message}"
                _loginSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}
