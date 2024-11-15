package tn.esprit.freelancy.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://172.18.2.223:3000/"

abstract class MyRetrofit {

    companion object {

        fun getRetrofit(): Retrofit {
            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }


}

