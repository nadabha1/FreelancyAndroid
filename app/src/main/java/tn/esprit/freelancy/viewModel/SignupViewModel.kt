package tn.esprit.freelancy.viewModel
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tn.esprit.freelancy.model.SignupData
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.repository.AuthRepository
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.State // Import manquant

class SignupViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    private val _password2 = MutableStateFlow("")
    val password2: StateFlow<String> = _password2


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
                // Vous pouvez stocker l'utilisateur ou son ID ici si nécessaire
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

}

