package tn.esprit.freelancy.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import tn.esprit.freelancy.model.user.Entity
import tn.esprit.freelancy.repository.CvRepository

class CvViewModel(private val repository: CvRepository) : ViewModel() {
    val analysisResult = MutableLiveData<List<Entity>>()

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

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
}
