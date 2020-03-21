package org.wirvsvirus.rkg.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RkgClient {

    private val service = Retrofit.Builder()
        .baseUrl("https://rkg-api-602.herokuapp.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RkgService::class.java)

    // TODO add client methods
}