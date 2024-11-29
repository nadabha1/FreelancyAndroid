package tn.esprit.freelancy.repository

import okhttp3.MultipartBody
import tn.esprit.freelancy.model.user.Entity
import tn.esprit.freelancy.remote.CvApiService

class CvRepository(private val api: CvApiService) {

    suspend fun uploadCv(file: MultipartBody.Part): List<Entity> {
        return api.uploadCv(file)
    }
}
