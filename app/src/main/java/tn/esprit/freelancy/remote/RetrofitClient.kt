package tn.esprit.freelancy.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.esprit.freelancy.remote.projet.ProjetAPI
import java.util.concurrent.TimeUnit

object RetrofitClient {
    //private const val BASE_URL = "http://172.16.8.108:3000/" // Replace with your backend URL
private const val BASE_URL = "http://192.168.1.125:3000/" // Replace with your backend URL
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Increase connection timeout
        .readTimeout(30, TimeUnit.SECONDS)    // Increase read timeout
        .writeTimeout(30, TimeUnit.SECONDS)   // Increase write timeout
        .build()
    val authService: UserAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
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
    val projetApi: ProjetAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProjetAPI::class.java)
    }

}