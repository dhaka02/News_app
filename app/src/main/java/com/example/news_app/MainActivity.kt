package com.example.news_app

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.news_app.ui.theme.News_AppTheme
import com.example.news_app.view_models.Main_ViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val news_categs= mutableListOf("Health","Sports","Technology","Business","Entertainment","Science")
//        val src_names= mutableListOf("Google News (India)", "The Hindu", "The Times of India")
//        val src_ids= mutableListOf("google-news-in", "the-hindu", "the-times-of-india")
        super.onCreate(savedInstanceState)
        val view_m=ViewModelProvider(this)[Main_ViewModel :: class.java]

        setContent {
            News_AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android",news_categs,view_m, this)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, news_categs : MutableList<String>, view_m : Main_ViewModel, context: Context, modifier: Modifier = Modifier) {
    val cont= LocalContext.current

    val icons : List<Int> = listOf(
        R.drawable.health5,
        R.drawable.sports,
        R.drawable.technology,
        R.drawable.business2,
        R.drawable.entertainment2,
        R.drawable.science
    )

    var iter = 0


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
                Row( modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.padding(10.dp))
                    IconButton(onClick = {},
                        modifier = Modifier.size(32.dp)) {
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
                            locationprov.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,
                                object :
                                    CancellationToken() {
                                    override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                                        CancellationTokenSource().token

                                    override fun isCancellationRequested() = false
                                })
                        var lat: Float? = null
                        var long: Float? = null


                        GlobalScope.launch {

                            loc.addOnSuccessListener {

                                lat = it.latitude.toFloat()
                                long = it.longitude.toFloat()

                                Log.d(
                                    "shivamLocation",
                                    "Latitde : ${it.latitude} Longitude : ${it.longitude}"
                                )

                            }
                           while(lat==null || long==null){}

                            val state = view_m.getstate(lat!!, long!!, context)

                            Log.d("shivamstate",state);




                            val int = Intent(cont, Headlines::class.java)

                            Log.d("debugg", "state -> $state")

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
        {
        it->
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(it)) {
        item {
//            Text(text = "Domains", fontSize = 32.sp,modifier= Modifier
//                .fillMaxWidth()
//                .padding(12.dp))
//            for (i in 0..<src_names.size){
//                Text(text = src_names[i], fontSize = 32.sp,modifier=Modifier.fillMaxWidth().padding(12.dp).clickable {
//                    val int = Intent(cont, Headlines::class.java)
//                    int.putExtra("Type","source")
//                    int.putExtra("domain",src_ids[i]);
//
//                    cont.startActivity(int)
//                })
//            }
            Spacer(modifier = Modifier.padding(12.dp))

            for (i in news_categs) {
                //Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(6.dp))
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .border(1.dp, Color.Gray, RoundedCornerShape(24.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(10.dp)
                                .size(50.dp)
                                .weight(75f)
                                .shadow(8.dp)
                                .clickable {
                                        val int = Intent(cont, Headlines::class.java)
                                        int.putExtra("category", i);
                                        cont.startActivity(int)
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Red
                            )
                        ) {

                            Text(text = i,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier
                                .padding(12.dp)
                            )

                        }
                        Image(
                            painter = painterResource(id = icons[iter]),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(4.dp)
                                .weight(25f)
                        )
                    }
                }

                iter = iter + 1
            }
        }
    }


    }

}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    News_AppTheme {
//        Greeting("Android")
//    }
//}