package com.instamealmobile.network

import com.instamealmobile.data.Recipe
import com.instamealmobile.data.ShoppingListUpdateResponse
import com.instamealmobile.data.SmallShoppingItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.Dictionary

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
    ) : Dictionary<String,String>

}
