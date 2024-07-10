package com.example.news_app.network

import com.example.news_app.data.Location
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Location_Int {

    @GET("/geo/1.0/reverse")
    suspend fun getLocation(@Query("lat") lat: Double,@Query("lon") long: Double,@Query("limit") limit : Int,@Query("appid") appid : String) : Response<Location>
}