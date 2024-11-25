package tn.esprit.freelancy.repository

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import tn.esprit.freelancy.model.AnalysisResponse
import tn.esprit.freelancy.model.Entity
import tn.esprit.freelancy.remote.CvApiService
import java.io.File

class CvRepository(private val api: CvApiService) {

    suspend fun uploadCv(file: MultipartBody.Part): List<Entity> {
        return api.uploadCv(file)
    }
}
