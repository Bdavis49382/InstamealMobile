package com.instamealmobile.network

import retrofit2.http.GET
import retrofit2.http.Header

interface BackendApiService {
    @GET("shoppingList")
    suspend fun getShoppingList(@Header("householdId") token: String) : List<String>
}
