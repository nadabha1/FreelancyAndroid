package tn.esprit.freelancy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tn.esprit.freelancy.model.user.ForgotPasswordRequest
import tn.esprit.freelancy.model.user.ResetPasswordRequest
import tn.esprit.freelancy.remote.RetrofitClient

class ForgotPasswordViewModel : ViewModel() {
    private val _otpFromServer = MutableStateFlow<String?>(null) // OTP reçu du backend
    private val otpFromServer: StateFlow<String?> = _otpFromServer

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _otp3 = MutableStateFlow("")
    val otp3: StateFlow<String> = _otp3

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
        _otp3.value = newOtp
    }

    fun onNewPasswordChange(newPassword: String) {
        _newPassword.value = newPassword
    }
    private val _otpDigits = MutableStateFlow(List(6) { "" }) // A list for 6 OTP fields
    val otpDigits: StateFlow<List<String>> = _otpDigits

    // Combine all digits into a single string
    val otp: String
        get() = _otpDigits.value.joinToString("")

    fun updateOtpDigit(index: Int, digit: String) {
        val newOtpDigits = _otpDigits.value.toMutableList()
        newOtpDigits[index] = digit.take(1) // Ensure only 1 character is stored
        _otpDigits.value = newOtpDigits
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
                    ResetPasswordRequest(username.value, otp, newPassword.value) // Use concatenated OTP
                )
                if (response.isSuccessful) {
                    _successMessage.value = "Password reset successfully."
                    _currentStep.value = 4 // Go to the success page
                    _errorMessage.value = null // Clear errors
                } else {
                    _errorMessage.value = response.message()
                    _successMessage.value = null // Clear success
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
                _successMessage.value = null // Clear success
            }
        }
    }

    fun verifyOtp() {
        viewModelScope.launch {
            val enteredOtp = otp // Concatenate the user-entered digits
            val serverOtp = _otpFromServer.value?.trim() // Trim spaces or formatting issues

            if (enteredOtp == serverOtp) {
                _successMessage.value = "OTP verified successfully."
                _errorMessage.value = null // Clear error message
                _currentStep.value = 3 // Proceed to the next step
            } else {
                _errorMessage.value = "Invalid OTP. Please try again."
                _successMessage.value = null // Clear success message
            }
        }
    }


}
