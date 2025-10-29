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

interface ShoppingListService {
    @GET("shopping-list/")
    @Headers("Cache-Control: no-cache")
    suspend fun getShoppingList() : List<ShoppingItem>
    @POST("shopping-list/")
    suspend fun postShoppingList(@Body item: ShoppingItem) : List<ShoppingItem>
    @GET("shopping-list/suggestions")
    suspend fun getSuggestions() : List<String>
    @POST("shopping-list/check/{id}")
    suspend fun checkItem(@Path("id") id: String) : List<ShoppingItem>
    @PUT("shopping-list/{id}")
    suspend fun editItem(
        @Path("id") id: String,
        @Body item: SmallShoppingItem) : List<ShoppingItem>
//    @PATCH("shopping-list/move")
//    suspend fun moveItem(
//        @Query("from_index") fromIndex: Int,
//        @Query("to_index") toIndex: Int
//    ): List<ShoppingItem>
    @PATCH("shopping-list/reorder")
    suspend fun reorder(
        @Body orderedList: List<ShoppingItem>
    ): List<ShoppingItem>
}
