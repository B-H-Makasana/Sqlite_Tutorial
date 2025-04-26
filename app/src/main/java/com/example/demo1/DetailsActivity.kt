package com.example.demo1

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.demo1.ui.theme.Demo1Theme
import kotlinx.coroutines.*

class DetailsActivity : ComponentActivity() {
    private val TAG = "DetailsActivity"

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = intent.getStringExtra("Email")
        Log.d(TAG, "onCreate() called with: savedInstanceState = $email")

        actionBar!!.title = "Details"
        setContent {
            Demo1Theme {
                showDetails(email = email!!)
            }
        }
    }

    @SuppressLint("Range", "CoroutineCreationDuringComposition")
    @Composable
    private fun showDetails(email: String) {
        val text = rememberSaveable {
            mutableStateOf("")
        }
        val mContext = LocalContext.current
        CoroutineScope.launch {
            val db = DBHelper(mContext)
            withContext(Dispatchers.IO) {
                text.value = db.getUser(email)
            }
        }
        Card(
            elevation = 4.dp,
            backgroundColor = Color(R.color.transparent),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = text.value, modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}
