package tn.esprit.freelancy.remote

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import tn.esprit.freelancy.model.AnalysisResponse
import tn.esprit.freelancy.model.Entity

interface CvApiService {

    @Multipart
    @POST("cv/upload") // Replace with your endpoint
    suspend fun uploadCv(
        @Part file: MultipartBody.Part
    ): List<Entity>}
