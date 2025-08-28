package com.instamealmobile.network

import com.instamealmobile.data.MenuItem
import com.instamealmobile.data.MenuListItem
import com.instamealmobile.data.Recipe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MenuService {
    @GET("menu/")
    suspend fun getMenu() : List<MenuListItem>
    @GET("menu/recipes/{recipe_id}")
    suspend fun getRecipe(@Path("recipe_id") recipe_id: String) : Recipe
    @GET("menu/online")
    @Headers("Cache-Control: public, max-age=" + 60 * 60 * 24) // cache lasts a whole day as these should almost never change
    suspend fun getRecipeOnline(@Query("link") link: String) : Recipe
    @GET("menu/index/{index}")
    suspend fun getRecipeByIndex( @Path("index") index: Int) : MenuItem
    @POST("menu/")
    suspend fun addRecipe(@Body menu_item: MenuItem) : List<MenuListItem>
    @POST("menu/finish/{recipe_id}")
    suspend fun finishMeal(
        @Path("recipe_id") recipe_id: String,
        @Query("rating") rating: Float?
    ) : List<MenuListItem>
    @PATCH("menu/index/{index}")
    suspend fun updateRecipeByIndex( @Path("index") index: Int, @Body menu_item: MenuItem) : MenuItem
}
