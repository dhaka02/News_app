package com.example.news_app.network

import com.example.news_app.data.News_Class
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface News_Int {

    @GET("/v2/top-headlines")
    suspend fun getNews(@Query("country") country:String, @Query("category") category: String, @Query("apiKey") apikey:String) : Response<News_Class>
    @GET("/v2/everything")
    suspend fun getNews_location(@Query("q") location: String, @Query("pageSize") pagesize: Int,  @Query("page") page: Int, @Query("apiKey") apikey: String): Response<News_Class>
}