package tn.esprit.freelancy.remote

import android.widget.Toast
import com.google.gson.Gson
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.esprit.freelancy.model.ApiError

object RetrofitClient {
    private const val BASE_URL = "http://172.16.8.43:3000/" // Replace with your backend URL
   // private const val BASE_URL = "http://192.168.0.136:3000/" // Replace with your backend URL

    val authService: UserAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserAPI::class.java)
    }


}