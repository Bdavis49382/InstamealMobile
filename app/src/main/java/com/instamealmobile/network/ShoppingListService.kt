package com.instamealmobile.network

import com.instamealmobile.data.ShoppingItem
import com.instamealmobile.data.ShoppingListUpdateResponse
import com.instamealmobile.data.SmallShoppingItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ShoppingListService {
    @GET("shopping-list")
    suspend fun getShoppingList(@Header("householdId") token: String) : List<ShoppingItem>
    @POST("shopping-list/add")
    suspend fun postShoppingList(@Header("householdId") token: String,@Body item: ShoppingItem) : ShoppingListUpdateResponse
    @POST("shopping-list/check/{index}")
    suspend fun checkItem(@Header("householdId") token: String,@Path("index") index: Int) : ShoppingListUpdateResponse
    @PUT("shopping-list/edit/{index}")
    suspend fun editItem(
        @Header("householdId") token: String,
        @Path("index") index: Int,
        @Body item: SmallShoppingItem) : ShoppingListUpdateResponse
}
