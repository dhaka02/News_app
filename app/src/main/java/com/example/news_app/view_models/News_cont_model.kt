package com.example.news_app.view_models

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class News_cont_model : ViewModel() {
    val intconn= MutableLiveData<Boolean>(false)
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
}