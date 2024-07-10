package com.example.news_app.view_models

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news_app.network.ApiFetcher
import com.example.news_app.data.Article

class HeadlineViewModel : ViewModel() {

    val news_titles =MutableLiveData<List<Article>>(listOf<Article>())
    val intconn=MutableLiveData<Boolean>(false)
    val apifetch= ApiFetcher();

    @SuppressLint("ServiceCast")
    fun checkNetworkConnectivity(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.d("Shivamconnection", "Found")
                intconn.postValue(true)
            }

            override fun onLost(network: Network) {
                Log.d("Shivamconnection", "lost")
                intconn.postValue(false)
            }
        })
    }
    suspend fun updatenews(location : String, country : String, category: String) {
        if(intconn.value == true){
            val newsList = apifetch.getnews(location, country, category)
            news_titles.postValue(newsList)


        }

//        news_titles.value=apifetch.getnews(location,country,category);

    }

    suspend fun updatenews_location(location: String, pagesize: Int) {
        val newsList=apifetch.getnews_location(location,pagesize)
        news_titles.postValue(newsList)
    }
}