package com.instamealmobile.network

import com.instamealmobile.data.Recipe
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface FeedService {
    @GET("feed/")
    @Headers("Cache-Control: no-cache")
    suspend fun getFeed(@Query("page") page: Int = 0) : List<Recipe>
    @GET("feed/search/")
    suspend fun searchFeed(@Query("query") query: String) : List<Recipe>
    @POST("feed/")
    suspend fun addRecipe(@Body recipe: Recipe) : String
    @PUT("feed/{recipe_id}")
    suspend fun updateRecipe(
        @Path("recipe_id") recipeId: String,
        @Body recipe: Recipe
    ) : String
    @Multipart
    @POST("feed/upload/image")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ) : String
    @GET("feed/tags")
    suspend fun getTags(): List<String>

}
