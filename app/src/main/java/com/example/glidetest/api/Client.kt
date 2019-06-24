package com.example.glidetest.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Client {
    var BASE_URL : String = "https://api.themoviedb.org/3/"
    var retrofit : Retrofit? = null
    fun getClient () : Retrofit? {
        if (retrofit == null)
        {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}