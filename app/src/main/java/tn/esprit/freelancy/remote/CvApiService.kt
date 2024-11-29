package tn.esprit.freelancy.remote

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import tn.esprit.freelancy.model.user.Entity

interface CvApiService {

    @Multipart
    @POST("cv/upload") // Replace with your endpoint
    suspend fun uploadCv(
        @Part file: MultipartBody.Part
    ): List<Entity>}
