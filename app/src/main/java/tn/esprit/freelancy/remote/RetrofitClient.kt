package tn.esprit.freelancy.remote

import android.widget.Toast
import com.google.gson.Gson
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.esprit.freelancy.model.ApiError

object RetrofitClient {
private const val BASE_URL = "http://192.168.1.71:3000/" // Replace with your backend URL

    val authService: UserAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserAPI::class.java)
    }
    val cvApiService: CvApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CvApiService::class.java)
    }

}