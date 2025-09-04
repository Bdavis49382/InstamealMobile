package com.instamealmobile.network

import com.instamealmobile.data.ShoppingItem
import com.instamealmobile.data.SmallShoppingItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ShoppingListService {
    @GET("shopping-list/")
    @Headers("Cache-Control: no-cache")
    suspend fun getShoppingList() : List<ShoppingItem>
    @POST("shopping-list/")
    suspend fun postShoppingList(@Body item: ShoppingItem) : List<ShoppingItem>
    @POST("shopping-list/check/{index}")
    suspend fun checkItem(@Path("index") index: Int) : List<ShoppingItem>
    @PUT("shopping-list/{index}")
    suspend fun editItem(
        @Path("index") index: Int,
        @Body item: SmallShoppingItem) : List<ShoppingItem>
    @PATCH("shopping-list/move")
    suspend fun moveItem(
        @Query("from_index") fromIndex: Int,
        @Query("to_index") toIndex: Int
    ): List<ShoppingItem>
}
