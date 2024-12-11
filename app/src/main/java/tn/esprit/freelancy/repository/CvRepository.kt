package tn.esprit.freelancy.repository

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import tn.esprit.freelancy.model.user.ApiResponse2
import tn.esprit.freelancy.model.user.Entity
import tn.esprit.freelancy.remote.CvApiService
import tn.esprit.freelancy.remote.RetrofitClient.authService
import java.io.File

class CvRepository(private val api: CvApiService) {

    suspend fun uploadCv(file: MultipartBody.Part): List<Entity> {
        return api.uploadCv(file)
    }
    suspend fun uploadCv2(userId: String,  filePart: MultipartBody.Part): ApiResponse2 {
        return authService.uploadCv(userId, filePart)
    }


}
