package com.example.demo1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.demo1.ui.theme.Demo1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar!!.title="Login"
        setContent {
            Demo1Theme {
               DoLogin()
            }
        }
    }

    @Composable
    fun DoLogin() {
        val email = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp, horizontal = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            EditText(title = "Email", KeyboardType.Email, email)
            EditText(title = "Password", KeyboardType.Password, password)
            LoginButton(
                email, password
            )
        }
    }

    @Composable
    private fun LoginButton(email: MutableState<String>, password: MutableState<String>) {
        val mContext = LocalContext.current
        Button(
            onClick = {
                val emailPattern = "^([\\w\\.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)$"
                val passwordPattern =
                    "^(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#\$%^&+=])" + "(?=\\S+\$).{8,20}\$"

                if (email.value == "" || password.value == "") {
                    Toast.makeText(mContext, "Please enter email or password", Toast.LENGTH_LONG)
                        .show()
                } else if (!email.value.trim { it <= ' ' }.matches(emailPattern.toRegex())) {
                    Toast.makeText(mContext, "Email should be valid", Toast.LENGTH_LONG).show()
                } else if (!password.value.matches(passwordPattern.toRegex()) || password.value.length < 8) {
                    Toast.makeText(mContext, "Please Enter valid password", Toast.LENGTH_LONG)
                        .show()
                } else {
                    var mlogin: Boolean
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            val db = DBHelper(mContext)
                            mlogin = db.isUserExists(email.value, password.value)
                        }
                        withContext(Dispatchers.Main) {
                            if (!mlogin) {
                                Toast.makeText(mContext, "User does not exists", Toast.LENGTH_LONG)
                                    .show()
                            } else {
                                val mintent = Intent(mContext, DetailsActivity::class.java).apply {
                                    putExtra("Email", email.value)
                                }
                                startActivity(mintent)
                                finish()
                            }
                        }
                    }
                }
            }, modifier = Modifier
                .padding(vertical = 16.dp)
        ) {
            Text(text = "Login", modifier = Modifier.padding(horizontal = 40.dp, vertical = 4.dp))
        }
    }

    @Composable
    fun EditText(title: String, keyboardType: KeyboardType, input: MutableState<String>) {
        OutlinedTextField(
            value = input.value,
            onValueChange = { input.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label =
            { Text(title) },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            textStyle = TextStyle(color = Color.Gray)
        )
    }
}