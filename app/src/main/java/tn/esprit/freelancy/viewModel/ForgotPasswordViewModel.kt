package tn.esprit.freelancy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tn.esprit.freelancy.model.ForgotPasswordRequest
import tn.esprit.freelancy.model.ResetPasswordRequest
import tn.esprit.freelancy.remote.RetrofitClient

class ForgotPasswordViewModel : ViewModel() {
    private val _otpFromServer = MutableStateFlow<String?>(null) // OTP reçu du backend
    private val otpFromServer: StateFlow<String?> = _otpFromServer

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _otp = MutableStateFlow("")
    val otp: StateFlow<String> = _otp

    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword

    private val _currentStep = MutableStateFlow(1) // Étape actuelle : 1 -> Email, 2 -> OTP, 3 -> Nouveau mot de passe
    val currentStep: StateFlow<Int> = _currentStep

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
    }

    fun onOtpChange(newOtp: String) {
        _otp.value = newOtp
    }

    fun onNewPasswordChange(newPassword: String) {
        _newPassword.value = newPassword
    }

    fun sendOtp() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authService.sendOtp(ForgotPasswordRequest(username.value))
                println(response.otp)
                if (response.otp.isNotEmpty()) {
                    _successMessage.value = "OTP sent to your email."
                    _currentStep.value = 2
                    _otpFromServer.value = response.otp
                } else {
                    _errorMessage.value = "failed"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun resetPassword() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authService.resetPassword(
                    ResetPasswordRequest(username.value, otp.value, newPassword.value)
                )
                println(response.body()?.otp)
                if (response.isSuccessful) {
                    _successMessage.value = "Password reset successfully."
                    _currentStep.value = 4 // Retour à l'étape initiale
                } else {
                    _errorMessage.value = response.message()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }
    fun verifyOtp() {
        viewModelScope.launch {
            if (_otp.value == _otpFromServer.value) {
                _successMessage.value = "OTP verified successfully."
                _currentStep.value = 3 // Passer à l'étape suivante (nouveau mot de passe)
            } else {
                _errorMessage.value = "Invalid OTP. Please try again."
            }
        }
    }

}
