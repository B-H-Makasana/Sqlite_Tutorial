package com.example.demo1

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardType.Companion.Email
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.demo1.ui.theme.Demo1Theme
import kotlinx.coroutines.*
import java.util.*

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar!!.title = "Register"
        setContent {
            Demo1Theme {
                Content()
            }
        }
    }
}

@Composable
fun Content() {
    val firstName = rememberSaveable { mutableStateOf("") }
    val lastName = rememberSaveable { mutableStateOf("") }
    val age = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val confirmPassword = rememberSaveable { mutableStateOf("") }
    val mDate = remember { mutableStateOf("") }
    val selectedOption = remember { mutableStateOf("") }

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp), verticalArrangement = Arrangement.SpaceAround
    ) {

        EditText(title = "First Name", KeyboardType.Text, firstName)
        EditText(title = "Last Name", KeyboardType.Text, lastName)
        EditText(title = "Age", KeyboardType.Decimal, age)
        EditText(title = "Email", KeyboardType.Email, email)
        DatePicker(mDate)
        SimpleRadioButtonComponent(selectedOption)
        Switcher()
        EditText(title = "Password", KeyboardType.Password, password)
        EditText(title = "Confirm Password", KeyboardType.Password, confirmPassword)
        SignUpButton(
            firstName,
            lastName,
            age,
            email,
            mDate,
            selectedOption,
            password,
            confirmPassword
        )
        LoginText()
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
            keyboardType = keyboardType,
        ),
        textStyle = TextStyle(color = Color.Gray)
    )
}

@Composable
fun DatePicker(mDate: MutableState<String>) {
    val mContext = LocalContext.current

    val mYear: Int
    val mMonth: Int
    val mDay: Int

    val mCalendar = Calendar.getInstance()

    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    val title = remember {
        mutableStateOf("Select Date")
    }

    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
        }, mYear, mMonth, mDay
    )

    Button(
        modifier = Modifier
            .padding(vertical = 8.dp),
        onClick = {
            mDatePickerDialog.show()
        }) {
        Row {
            Text(
                text = title.value,
                color = Color(R.color.white),
                modifier = Modifier.padding(top = 2.dp, end = 3.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                contentDescription = null,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
    if (mDate.value != "") {
        title.value = mDate.value
    }
}

@Composable
fun SimpleRadioButtonComponent(mselectedOption: MutableState<String>) {
    val radioOptions = listOf("Female", "Male")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf("") }

    Row {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) }
                    )
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = {
                        mselectedOption.value = text
                    }
                )

                Text(
                    text = text,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}


@Composable
fun Switcher() {
    val mCheckedState = remember { mutableStateOf(false) }
    Row {
        Text(
            text = "Do you want to receive notification on gmail",
            Modifier.padding(top = 8.dp, end = 4.dp),
            color = Color(R.color.colorPrimary),
            fontWeight = FontWeight.SemiBold
        )
        Switch(checked = mCheckedState.value, onCheckedChange = { mCheckedState.value = it })
    }
}

@Composable
fun LoginText() {
    val mContext = LocalContext.current
    Row {
        Text(
            text = "Already have an account",
            color = Color(R.color.transparent),
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
        )
        Text(
            text = "Login",
            color = Color(R.color.transparent),
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 8.dp)
                .clickable(true, onClick = {
                    val intent = Intent(mContext, LoginActivity::class.java)
                    mContext.startActivity(intent)
                })
        )
    }
}

@Composable
fun SignUpButton(
    firstName: MutableState<String>,
    lastName: MutableState<String>,
    age: MutableState<String>,
    email: MutableState<String>,
    date: MutableState<String>,
    gender: MutableState<String>,
    password: MutableState<String>,
    confirmPassword: MutableState<String>
) {
    val mContext = LocalContext.current
    Button(
        onClick = {
            val emailPattern = "[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
            val passwordPattern =
                "^(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$"

            val name = "^[a-zA-Z]+$"
            val rgAge = "^[0-9]*$"

            if (firstName.value == "" || lastName.value == "" || age.value == "" || email.value == "" || date.value == "" || gender.value == "") {
                Toast.makeText(mContext, "Please enter all details", Toast.LENGTH_LONG).show()
            } else if (!email.value.trim { it <= ' ' }.matches(emailPattern.toRegex())) {
                Toast.makeText(mContext, "Email should be valid", Toast.LENGTH_LONG).show()
            } else if (!password.value.matches(passwordPattern.toRegex()) || password.value.length < 8) {
                Toast.makeText(mContext, "Please Enter valid password", Toast.LENGTH_LONG).show()
            } else if (password.value != confirmPassword.value) {
                Toast.makeText(mContext, "Password not matches", Toast.LENGTH_LONG).show()
            } else if (age.value.length > 3 || !age.value.matches(rgAge.toRegex()) || age.value.toInt() <= 0) {
                Toast.makeText(mContext, "Please Enter valid Age", Toast.LENGTH_LONG).show()

            } else if (!(firstName.value.matches(name.toRegex())) || !(lastName.value.matches(name.toRegex()))) {
                Toast.makeText(mContext, "Name should be character only", Toast.LENGTH_LONG).show()
            } else {
                var isUserExists: Boolean = false

                val db = DBHelper(mContext)
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        val cursor = db.getUser(email.value)
                        if (cursor.count() > 1) {
                            Log.d("TAG", "user esist")
                            isUserExists = true
                        } else {
                            val user = User(
                                firstName.value,
                                lastName.value,
                                age.value.toInt(),
                                email.value,
                                date.value,
                                gender.value,
                                password.value
                            )
                            db.addUser(user)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        delay(1000)
                        if (isUserExists == true) {
                            Toast.makeText(mContext, "User Already Exists", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            val intent = Intent(mContext, DetailsActivity::class.java).apply {
                                putExtra("Email", email.value)
                            }
                            startActivity(mContext, intent, null)
                            firstName.value = ""
                            lastName.value = ""
                            age.value = ""
                            email.value = ""
                            date.value = ""
                            gender.value = "Birth Date"
                            password.value = ""
                            confirmPassword.value = ""
                        }
                    }
                }
            }
        }, modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        Text(text = "Sign up", modifier = Modifier.padding(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Demo1Theme {
        Content()
    }
}