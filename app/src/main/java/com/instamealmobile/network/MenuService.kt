package com.instamealmobile.network

import com.instamealmobile.data.MenuItem
import com.instamealmobile.data.Recipe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Dictionary

interface MenuService {
    @GET("menu/get/recipe")
    suspend fun getRecipe(@Header("householdId") token: String,@Query("recipe_id") recipe_id: String) : Recipe
    @GET("menu/get/recipe-online")
    suspend fun getRecipeOnline(@Header("householdId") token: String,@Query("link") link: String) : Recipe
    @POST("menu/add/{user_id}")
    suspend fun addRecipe(
        @Header("householdId") token: String,
        @Path("user_id") user_id: String,
        @Body menu_item: MenuItem) : Dictionary<String,String>
}
