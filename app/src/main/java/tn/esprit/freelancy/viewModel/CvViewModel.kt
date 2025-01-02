package tn.esprit.freelancy.viewModel

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import tn.esprit.freelancy.googleSign.USERS_COLLECTION
import tn.esprit.freelancy.model.user.ApiResponse2
import tn.esprit.freelancy.model.user.Entity
import tn.esprit.freelancy.model.user.GetUserIdRequest
import tn.esprit.freelancy.model.user.GetUserIdResponse
import tn.esprit.freelancy.model.user.GetUserResponsetest
import tn.esprit.freelancy.model.user.UserProfile
import tn.esprit.freelancy.model.user.UserProfileComplet
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.repository.AuthRepository
import tn.esprit.freelancy.repository.CvRepository
import tn.esprit.freelancy.viewModel.chat.ChatViewModel
import java.io.File

class CvViewModel(private val repository: CvRepository) : ViewModel() {
    val analysisResult = MutableLiveData<List<Entity>>()
    val userRepository =AuthRepository (RetrofitClient.authService)

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> get() = _errorMessage

    fun uploadCv(context: Context, fileUri: Uri, fileName: String) {
        viewModelScope.launch {
            try {
                // Resolve URI to InputStream
                val inputStream = context.contentResolver.openInputStream(fileUri)
                val fileBytes = inputStream?.readBytes() ?: throw Exception("Failed to read file")
                inputStream.close()

                // Create MultipartBody.Part
                val requestBody = fileBytes.toRequestBody("application/pdf".toMediaTypeOrNull())
                val filePart = MultipartBody.Part.createFormData("file", fileName, requestBody)

                val response = repository.uploadCv(filePart)
                analysisResult.postValue(response)
            } catch (e: Exception) {
                _errorMessage.postValue("Failed to upload CV: ${e.message}")
            }
        }
    }
    fun getRealPathFromURI(context: Context, uri: Uri): String? {
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndex("_data")
            if (columnIndex != -1) {
                it.moveToFirst()
                return it.getString(columnIndex)
            }
        }
        return uri.path // Fallback to the raw path
    }

    ///////////////////////////////////2
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> get() = _uiState

    fun uploadCv2(userId: String, file: File) {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
                val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)
                val response = repository.uploadCv2(userId, filePart)
                _uiState.value = UiState.Success(response)
                println("Response: $response")
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class Success(val response: ApiResponse2) : UiState()
        data class Error(val message: String) : UiState()
    }
    fun updateSkills(userId: String, skills: List<String>) {
        viewModelScope.launch {
            try {
                val cleanedSkills = skills.map { it.replace("\n", " ").trim() }
                println("Cleaned skills to send: $cleanedSkills")
                val response = userRepository.updateUserSkills(userId, cleanedSkills)
                _uiState.value = UiState.Success(response)
                userProfile.value?.let {
                    adduserToFirestore(it) }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to update skills")
            }
        }
    }

    private val userCollection = Firebase.firestore.collection(USERS_COLLECTION)

    fun adduserToFirestore(signupRequest: UserProfileComplet){
        val userDataMap = mapOf(
            "userId" to signupRequest.idUser,
            "username" to signupRequest.username,
            "email" to signupRequest.email,
            "role" to signupRequest.role,
            "skills" to signupRequest.skills
        )
        val userDocument = userCollection.document(signupRequest.idUser)
        userDocument.get()
            .addOnSuccessListener {
                if (it.exists()) {
                    userDocument.update(userDataMap)
                } else {
                    userDocument.set(userDataMap).addOnSuccessListener {
                        Log.d(ContentValues.TAG, "User added successfully")
                    }
                        .addOnFailureListener { e ->
                            Log.d(ContentValues.TAG, "Error adding user: $e")
                        }
                }
            }
    }


    private val _userProfile = MutableStateFlow<UserProfileComplet?>(null)
    val userProfile: StateFlow<UserProfileComplet?> = _userProfile
    private val _user = MutableStateFlow<GetUserResponsetest?>(null)
    val user: StateFlow<GetUserResponsetest?> = _user
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    fun fetchUser(username: String) {

        viewModelScope.launch {
            try {
                println("Fetching user profile for email: $username") // Debug log
                val user = RetrofitClient.authService.getUserProfile(username)
                println("User fetched: $user") // Debug log
                _userProfile.value = user // Assign the user to StateFlow
                println("User profile fetched successfully $userProfile") // Debug log

            } catch (e: Exception) {
                println("Error fetching user profile: ${e.message}") // Debug log
                _userProfile.value = null
            }
        }
    }


}



