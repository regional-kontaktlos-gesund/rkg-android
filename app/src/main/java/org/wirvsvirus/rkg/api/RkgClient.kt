package org.wirvsvirus.rkg.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RkgClient {

    val service = Retrofit.Builder()
        .baseUrl("https://rkg-api-602.herokuapp.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT).setLevel(
                        HttpLoggingInterceptor.Level.BODY
                    )
                )
                .build()
        )
        .build()
        .create(RkgService::class.java)
}