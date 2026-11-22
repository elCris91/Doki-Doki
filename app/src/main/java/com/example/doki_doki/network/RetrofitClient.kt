package com.example.doki_doki.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:0kwo9KBz/"

    val apiService: XanoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(XanoApiService::class.java)
    }
}