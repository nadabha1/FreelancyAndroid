package tn.esprit.freelancy.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.esprit.freelancy.remote.projet.ProjetAPI

object RetrofitClient {
private const val BASE_URL = "http://192.168.1.40:3000/" // Replace with your backend URL
//private const val BASE_URL = "http://172.18.6.197:3000/" // Replace with your backend URL

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
    val projetApi: ProjetAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProjetAPI::class.java)
    }

}