package com.instamealmobile.network

import com.instamealmobile.data.Recipe
import retrofit2.http.GET
import retrofit2.http.Header

interface FeedService {
    @GET("feed/get")
    suspend fun getFeed(@Header("householdId") token: String) : List<Recipe>
}
