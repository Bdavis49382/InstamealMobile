package com.instamealmobile.network

import com.instamealmobile.data.ShoppingItem
import com.instamealmobile.data.SmallShoppingItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ShoppingListService {
    @GET("shopping-list/")
    suspend fun getShoppingList() : List<ShoppingItem>
    @POST("shopping-list/")
    suspend fun postShoppingList(@Body item: ShoppingItem) : List<ShoppingItem>
    @POST("shopping-list/check/{index}")
    suspend fun checkItem(@Path("index") index: Int) : List<ShoppingItem>
    @PUT("shopping-list/{index}")
    suspend fun editItem(
        @Path("index") index: Int,
        @Body item: SmallShoppingItem) : List<ShoppingItem>
}
