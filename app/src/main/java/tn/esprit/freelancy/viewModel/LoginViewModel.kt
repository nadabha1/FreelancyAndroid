package tn.esprit.freelancy.viewModel


import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tn.esprit.freelancy.model.chat.AppState
import tn.esprit.freelancy.model.chat.SignInResult
import tn.esprit.freelancy.model.chat.UserData
import tn.esprit.freelancy.model.user.GetRoleIdResponse
import tn.esprit.freelancy.model.user.GetUserIdResponse
import tn.esprit.freelancy.model.user.GetUserResponsetest
import tn.esprit.freelancy.model.user.LoginRequest
import tn.esprit.freelancy.model.user.UserProfileComplet
import tn.esprit.freelancy.model.user.UserProfileFireBase
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.session.SessionManager

class
LoginViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email


    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _loginFre = MutableStateFlow(false)
    val loginFre: StateFlow<Boolean> = _loginFre
    private val _loginEntrep = MutableStateFlow(false)
    val loginEntrep: StateFlow<Boolean> = _loginEntrep
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    private val _firebaseUid = MutableStateFlow<String>("")
    val firebaseUid: StateFlow<String> = _firebaseUid
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }
    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }
    @SuppressLint("SuspiciousIndentation")
    fun login() {
        viewModelScope.launch {
            _isLoading.value = true
            val auth = FirebaseAuth.getInstance()


            try {
                auth.signInWithEmailAndPassword(_email.value, _password.value)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser = task.result?.user

                            val uid = firebaseUser?.uid
                            if (uid != null) {
                                _firebaseUid.value = uid
                            } // Store the Firebase UID

                            println("Login successful, Firebase UID: $uid")}
                        else {
                            println(task.exception?.message ?: "Login failed")
                            _errorMessage.value = "Login failed"
                            _loginSuccess.value = false
                        }}
                val success = sessionManager.authenticate(_email.value, _password.value,_firebaseUid.value)
                if (success) {


                    // Fetch user profile and role
                    val responseBody = RetrofitClient.authService.getUserProfile(_email.value)
                    val id = responseBody.idUser
                    println("User ID in login : $id")

                    val roleName = RetrofitClient.authService.getRoleName(GetUserIdResponse(id))

                                if (roleName.idRole == "Freelancer") {
                                    _loginFre.value = true
                                } else {
                                    _loginFre.value = false
                                }

                                if (roleName.idRole == "Entrepreneur") {
                                    _loginEntrep.value = true
                                } else {
                                    _loginEntrep.value = false
                                }

                                _errorMessage.value = null
                                _loginSuccess.value = true

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

    private val _userProfile2 = MutableStateFlow<UserProfileComplet?>(null)
    val userProfile2: StateFlow<UserProfileComplet?> = _userProfile2
    private val _role = MutableStateFlow<GetRoleIdResponse?>(null)
    val role: StateFlow<GetRoleIdResponse?> = _role

    private val _user = MutableStateFlow<GetUserResponsetest?>(null)
    val user: StateFlow<GetUserResponsetest?> = _user
    fun fetchUserProfile(email: String): String {
        viewModelScope.launch {
            try {
                println("Fetching user profile for email: $email") // Debug log
                val user = RetrofitClient.authService.getUserProfile(email)
                println("User fetched: $user") // Debug log
                _userProfile2.value = user // Assign the user to StateFlow

                println("User profile fetched successfully") // Debug log
                val roleName = RetrofitClient.authService.getRoleName(GetUserIdResponse(user.idUser))
                _role.value = roleName
                println("Role fetched successfully with name: ${_role.value?.idRole}") // Debug log

            } catch (e: Exception) {
                println("Error fetching user profile: ${e.message}") // Debug log
                _userProfile2.value = null
            }
        }
        return role.value?.idRole ?: "feregh"
    }

}
