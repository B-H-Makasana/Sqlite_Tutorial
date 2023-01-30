package com.example.demo1

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.demo1.ui.theme.Demo1Theme

class DetailsActivity : ComponentActivity() {
    private  val TAG = "DetailsActivity"
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = intent.getStringExtra("Email")
        Log.d(TAG, "onCreate() called with: savedInstanceState = $email")


            setContent {
            Demo1Theme() {


                Surface(
                    modifier = Modifier.fillMaxSize(),

                    ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Details") },
                            )
                        },
                        content = { Showdetails(email!!) }

                    )
                }
            }
        }
    }

    @SuppressLint("Range", "CoroutineCreationDuringComposition")
    @Composable
    private fun Showdetails(email:String) {
        var text:String?=null
        var mContext= LocalContext.current


             val db = DBHelper(mContext)


              text = db.getUser(email)


        text?.let {
            Card(
                elevation = 4.dp,
                backgroundColor = Color(R.color.transparent),
                modifier = Modifier.padding(16.dp)
            ){

                Text(
                    text = it, modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

            }
        }



//        GlobalScope.launch{
//            withContext(Dispatchers.IO){
//                val db = DBHelper(mContext)
//            }
//
//           withContext(Dispatchers.Main){
//                delay(1000)
//            }
//            }
//        text?.let {
//            Card(
//                elevation = 4.dp,
//                backgroundColor = Color(R.color.transparent),
//                modifier = Modifier.padding(16.dp)
//            ){
//
//                Text(
//                    text = it, modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                )
//
//            }
//        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        Demo1Theme {
            Showdetails("bansi")
        }
    }

    override fun onRestart() {
        finish()
        super.onRestart()
    }
}
