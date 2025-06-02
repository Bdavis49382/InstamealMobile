package com.instamealmobile.network

import com.instamealmobile.data.Recipe
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FeedService {
    @GET("feed/get")
    suspend fun getFeed(@Header("householdId") token: String) : List<Recipe>
    @GET("feed/search/{query}")
    suspend fun searchFeed(@Header("householdId") token: String,@Path("query") query: String) : List<Recipe>
    @POST("feed/add/{user_id}")
    suspend fun addRecipe(
        @Header("householdId") token: String,
        @Path("user_id") userId: String,
        @Body recipe: Recipe
    ) : String
    @POST("feed/update/{user_id}/{recipe_id}")
    suspend fun updateRecipe(
        @Path("user_id") userId: String,
        @Path("recipe_id") recipeId: String,
        @Body recipe: Recipe
    ) : String
    @Multipart
    @POST("feed/upload/image")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ) : String

}
