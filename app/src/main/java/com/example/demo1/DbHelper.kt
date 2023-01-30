package com.example.demo1

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DBHelper(context: Context) :
     SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase){
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + EMAIL + " TEXT PRIMARY KEY, " +
                FNAME + " TEXT," +
                LNAME + " TEXT," +
                AGE + " INTEGER," +
                BDATE + " TEXT," +
                GENDER + " TEXT," +
                PASSWORD+ " TEXT" + ")")

        db.execSQL(query)
    }




    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

   suspend fun addUser(user: User){

        val values = ContentValues()


        values.put(FNAME, user.firstName)
        values.put(LNAME, user.lastName)
        values.put(AGE,user.age)
        values.put(EMAIL,user.email)
        values.put(BDATE,user.date)
        values.put(GENDER,user.gender)
        values.put(PASSWORD,user.password)


        val db = this.writableDatabase

        db.insert(TABLE_NAME, null, values)


        db.close()
    }


   @SuppressLint("Range")
    fun getUser(email:String): String{


        lateinit var text:String
        val db = this.readableDatabase

        var job=GlobalScope.launch(Dispatchers.IO) {
            var selectionArgs = listOf<String>(email)

            var cursor = db.rawQuery(
                "SELECT * FROM $TABLE_NAME WHERE $EMAIL= ?",
                selectionArgs.toTypedArray()
            )
            cursor?.let {
                Log.d("LoginDetail", "Showdetails: " + it.toString())
                if (cursor!!.moveToFirst()) {
                    var fname = cursor.getString(it.getColumnIndex(FNAME))
                    var lname = it.getString(it.getColumnIndex(LNAME))
                    var email = it.getString(it.getColumnIndex(EMAIL))

                    var age = it.getInt(it.getColumnIndex(AGE))
                    var gender = it.getString(it.getColumnIndex(GENDER))
                    var date = it.getString(it.getColumnIndex(BDATE))

                    text =
                        "Name : " + fname + " " + lname + "\n" + "Email : " + email + "\n" + "Age : " + age + "\n" + "Gender : " + gender + "\n" + "Birth Date : " + date
                    Log.d(
                        "DetailsActivity",
                        "Showdetails() called with: email = $email text is $text"
                    )
                    it.close()

                }
            }
        }
       runBlocking {
           job.join()
       }

       return text


   }
    @SuppressLint("Range")
    suspend fun isUserExists(email: String, password:String):Boolean{
        val selectionArgs= listOf<String>(email,password)

        val db = this.readableDatabase

        val cursor= db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $EMAIL= ? and $PASSWORD = ?",
            selectionArgs.toTypedArray()
        )

        if(cursor.count>0){
           return true
        }
        return false

    }



    companion object{

        private val DATABASE_NAME = "USER_DETAILS"

        private val DATABASE_VERSION = 1

        val TABLE_NAME = "user"

        val FNAME = "firstname"

        val LNAME="lastname"

        val EMAIL="email"

        val GENDER="gender"

        val BDATE="Birthdate"

        val PASSWORD="password"

        val AGE = "age"


    }
}
