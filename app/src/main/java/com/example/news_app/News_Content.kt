package com.example.news_app

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.news_app.ui.theme.News_AppTheme
import com.example.news_app.view_models.HeadlineViewModel
import com.example.news_app.view_models.Main_ViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class News_Content : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val desc=intent.getStringExtra("Description")
        val img_uri=intent.getStringExtra("image_uri")
        val web_url=intent.getStringExtra("weburl")

        val view_m=ViewModelProvider(this)[Main_ViewModel :: class.java]

        val view_mod_cont=ViewModelProvider(this)[HeadlineViewModel :: class.java]
        view_mod_cont.checkNetworkConnectivity(this)

        setContent {
            News_AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting3("Android",web_url,view_m, view_mod_cont,this)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting3(name: String, web_url: String?, view_m: Main_ViewModel, view_mod_cont: HeadlineViewModel, context: Context, modifier: Modifier = Modifier) {

    val cont = LocalContext.current
    val intconn by view_mod_cont.intconn.observeAsState();

    if(!intconn!!){
        Column(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(id = R.drawable.conn),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(4.dp)

            )
            Text(text = "Please check your network connnection")

        }
    }
    else {
        Scaffold(modifier = Modifier.fillMaxSize(),
            containerColor = Color(0xFFFFFAF0),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0XFFC70039),
                        titleContentColor = Color.White
                    ),
                    title = {
                        Text(
                            text = "GhatakNews",
                            style = TextStyle(fontStyle = FontStyle.Italic, fontSize = 30.sp)
                        )
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(60.dp)
                        .shadow(12.dp)
                        .border(
                            BorderStroke(1.dp, Color.Black),
                            shape = RoundedCornerShape(0.dp),
                        ),
                    containerColor = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.padding(10.dp))
                        IconButton(
                            onClick = {

                                val int = Intent(cont, MainActivity::class.java)
                                cont.startActivity(int)

                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.home),
                                contentDescription = "Home"
                            )
                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                        IconButton(onClick = {

                            val locationprov =
                                LocationServices.getFusedLocationProviderClient(cont as Activity);

                            if (ActivityCompat.checkSelfPermission(
                                    cont.applicationContext,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ) == PackageManager.PERMISSION_DENIED
                            ) {
                                ActivityCompat.requestPermissions(
                                    cont as Activity, arrayOf(
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    ), 101
                                )
                            }
                            val loc =
                                locationprov.getCurrentLocation(
                                    Priority.PRIORITY_HIGH_ACCURACY,
                                    object :
                                        CancellationToken() {
                                        override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                                            CancellationTokenSource().token

                                        override fun isCancellationRequested() = false
                                    })
                            var lat: Float? = null;
                            var long: Float? = null;


                            GlobalScope.launch {

                                loc.addOnSuccessListener {

                                    lat = it.latitude.toFloat()
                                    long = it.longitude.toFloat()

                                    Log.d(
                                        "shivamLocation",
                                        "Latitde : ${it.latitude} Longitude : ${it.longitude}"
                                    )


                                }
                                while (lat == null || long == null) {
                                }

                                val state = view_m.getstate(lat!!, long!!, context)


                                val int = Intent(cont, Headlines::class.java)

                                int.putExtra("category", "Local News");
                                int.putExtra("state", state)
                                cont.startActivity(int)

                            }

                        }, modifier = Modifier.size(32.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.local6),
                                contentDescription = "Local News"
                            )
                        }
                        Spacer(modifier = Modifier.padding(10.dp))

                    }
                }

            }
        )
        { it ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {

                item {

                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { context ->
                            WebView(context).apply {

                                webViewClient = WebViewClient() // Ensures links stay in the WebView
                                web_url?.let { loadUrl(it) } // Load the specified URL
                            }
                        }

                    )
                }
            }
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    News_AppTheme {
//        Greeting3("Android")
//    }
//}