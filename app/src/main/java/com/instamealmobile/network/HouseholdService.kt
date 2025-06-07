package com.instamealmobile.network

import com.instamealmobile.data.User
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path


interface HouseholdService {
    @GET("household/get")
    suspend fun getHousehold() : List<User>
    @GET("household/code")
    suspend fun getHouseholdCode() : String
    @GET("household/join/{user_id}/{code}")
    suspend fun joinHousehold(@Path("user_id") userId: String, @Path("code") code: String) : List<User>
    @DELETE("household/kick/{user_id}")
    suspend fun kickUser(@Path("user_id") userId: String) : List<User>
}