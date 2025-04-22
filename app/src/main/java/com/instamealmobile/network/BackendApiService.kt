package com.instamealmobile.network
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "localhost:4000"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface BackendApiService {
    @GET("shoppingList")
    suspend fun getShoppingList() : String
}

object BackendAPI {
    val retrofitService : BackendApiService by lazy {
        retrofit.create(BackendApiService::class.java)
    }
}