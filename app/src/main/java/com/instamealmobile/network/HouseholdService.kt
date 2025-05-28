package com.instamealmobile.network

import com.instamealmobile.data.User
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path


interface HouseholdService {
    @GET("household/get")
    suspend fun getHousehold(@Header("householdId") token: String) : List<User>
    @GET("household/get/{user_id}")
    suspend fun getHouseholdId( @Path("user_id") userId: String) : IdResponse
    @GET("household/code")
    suspend fun getHouseholdCode(@Header("householdId") token: String) : String
    @GET("household/join/{user_id}/{code}")
    suspend fun joinHousehold(@Path("user_id") userId: String, @Path("code") code: String) : List<User>
    @DELETE("household/kick/{user_id}")
    suspend fun kickUser(@Header("householdId") token: String, @Path("user_id") userId: String) : List<User>
}

data class IdResponse(val message: String, val household_id: String)
