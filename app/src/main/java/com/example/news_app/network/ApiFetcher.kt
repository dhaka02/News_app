package com.example.news_app.network


import android.util.Log
import com.example.news_app.data.Article
import com.example.news_app.data.LocationItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiFetcher {
    private val base_url="https://newsapi.org"
    private val loc_url="https://api.openweathermap.org"

    private val retobj= Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build()

    private val locobj=Retrofit.Builder().baseUrl(loc_url).addConverterFactory(GsonConverterFactory.create()).build()
    private val newsapi=retobj.create(News_Int :: class.java)
    private val locapi=locobj.create(Location_Int ::class.java)

    private val apikey="22b0342f4bd74a658324ab5aacc09f17";

    private val locapikey="f39977e6aa2d7bbe2d490041c66706f2";

    private val loclimit=1;
    private val page=1;



    suspend fun getnews(location : String,country:String,category: String) : List<Article> {


        val result=newsapi.getNews(country,category,apikey)

        Log.d("ApiFetcher", "API call successful. Response: ${result.body()}")

        if(result.body()==null){
            return listOf<Article>()
        }
        return result.body()?.articles!!;







//        var news_titles = mutableListOf<String>();
//
//        if(result.body()?.status=="ok"){
//
//            val tot_arts= result.body()!!.totalResults;
//
//            val news_articles= result.body()!!.articles;
//
//            for (i in news_articles){
//                news_titles.add(i.title);
//            }
//
//
//
//
//
//        }
//        return news_titles;


    }

    suspend fun getnews_location( location: String, pagesize: Int): List<Article> {
        val result=newsapi.getNews_location(location,pagesize,page,apikey)

        Log.d("ApiFetcher", "API call successful. Response: ${result.body()}")

        if(result.body()==null){
            return listOf<Article>()
        }
        return result.body()?.articles!!;
    }

    suspend fun getlocationinfo(lat : Float,long:Float) : LocationItem {
        val result=locapi.getLocation(lat.toDouble(),long.toDouble(),loclimit,locapikey)
        Log.d("ApiFetcher", "API Location call successful. Response: ${result.body()} at $lat and $long")

        val loc_item= result.body()?.get(0);

        return loc_item !!

    }


}