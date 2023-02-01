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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontLoadingStrategy.Companion.Async
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.demo1.ui.theme.Demo1Theme
import kotlinx.coroutines.*

class DetailsActivity : ComponentActivity() {
    private  val TAG = "DetailsActivity"
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = intent.getStringExtra("Email")
        Log.d(TAG, "onCreate() called with: savedInstanceState = $email")

         actionBar!!.title="Details"
            setContent {
            Demo1Theme() {


                Showdetails(email = email!!)
            }
        }
    }

    @SuppressLint("Range", "CoroutineCreationDuringComposition")
    @Composable
    private fun Showdetails(email:String) {
        var text= rememberSaveable {
            mutableStateOf("")
        }
        val mContext= LocalContext.current



             GlobalScope.launch {
                 val db = DBHelper(mContext)

                 withContext(Dispatchers.IO) {
                     text.value = db.getUser(email)
                 }
                 Log.d(TAG, "Showdetails: " + text)


                 withContext(Dispatchers.Main) {
                     Log.d(TAG, "withContext main")
                 }

             }
        Log.d(TAG, "outofGlobalScope")
            Card(
                elevation = 4.dp,
                backgroundColor = Color(R.color.transparent),
                modifier = Modifier.padding(16.dp)
            ){

                Text(
                    text = text.value, modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )


        }



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
