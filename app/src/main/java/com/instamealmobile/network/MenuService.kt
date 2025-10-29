package com.instamealmobile.network

import com.instamealmobile.data.MenuItem
import com.instamealmobile.data.MenuListItem
import com.instamealmobile.data.Recipe
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MenuService {
    @GET("menu/")
    suspend fun getMenu(@Header("Cache-Control") allowCache: String = "public, max-age=60") : List<MenuListItem>
    @GET("menu/recipes/{recipe_id}")
    suspend fun getRecipe(@Path("recipe_id") recipe_id: String) : Recipe
    @GET("menu/online")
    @Headers("Cache-Control: public, max-age=" + 60 * 60 * 24) // cache lasts a whole day as these should almost never change
    suspend fun getRecipeOnline(@Query("link") link: String) : Recipe
    @GET("menu/recipeId/{recipe_id}")
    suspend fun getRecipeById( @Path("recipe_id") recipeId: String,@Header("Cache-Control") allowCache: String = "public, max-age=60") : MenuItem
    @POST("menu/")
    suspend fun addRecipe(@Body menu_item: MenuItem) : List<MenuListItem>
    @DELETE("menu/{recipe_id}")
    suspend fun deleteMenuItem(@Path("recipe_id") recipe_id: String) : List<MenuListItem>
    @POST("menu/finish/{recipe_id}")
    suspend fun finishMeal(
        @Path("recipe_id") recipe_id: String,
        @Query("rating") rating: Float?
    ) : List<MenuListItem>
    @PATCH("menu/recipeId/{recipe_id}")
    suspend fun updateRecipeByRecipeId(@Path("recipe_id") recipeId: String, @Body menu_item: MenuItem) : MenuItem
}
