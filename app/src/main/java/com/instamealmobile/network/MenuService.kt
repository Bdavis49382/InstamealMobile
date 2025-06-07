package com.instamealmobile.network

import com.instamealmobile.data.MenuItem
import com.instamealmobile.data.Recipe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MenuService {
    @GET("menu/get")
    suspend fun getMenu() : List<MenuItem>
    @GET("menu/get/recipe")
    suspend fun getRecipe(@Query("recipe_id") recipe_id: String) : Recipe
    @GET("menu/get/recipe-online")
    suspend fun getRecipeOnline(@Query("link") link: String) : Recipe
    @GET("menu/get/recipe/{index}")
    suspend fun getRecipeByIndex( @Path("index") index: Int) : MenuItem
    @POST("menu/add/{user_id}")
    suspend fun addRecipe(
        @Path("user_id") user_id: String,
        @Body menu_item: MenuItem) : List<MenuItem>
    @POST("menu/finish")
    suspend fun finishMeal(
        @Query("recipe_id") recipe_id: String,
        @Query("user_id") user_id: String,
        @Query("rating") rating: Float?
    ) : List<MenuItem>
}
