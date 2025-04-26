package com.example.demo1

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.*

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + EMAIL + " TEXT PRIMARY KEY, " +
                FIRST_NAME + " TEXT," +
                LAST_NAME + " TEXT," +
                AGE + " INTEGER," +
                BIRTH_DATE + " TEXT," +
                GENDER + " TEXT," +
                PASSWORD + " TEXT" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addUser(user: User) {
        val values = ContentValues()

        values.put(FIRST_NAME, user.firstName)
        values.put(LAST_NAME, user.lastName)
        values.put(AGE, user.age)
        values.put(EMAIL, user.email)
        values.put(BIRTH_DATE, user.date)
        values.put(GENDER, user.gender)
        values.put(PASSWORD, user.password)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }


    @SuppressLint("Range")
    suspend fun getUser(mEmail: String): String {
        var text = ""
        val cursor: Cursor
        val db = this.readableDatabase
        val selectionArgs = listOf<String>(mEmail)
        cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $EMAIL= ?",
            selectionArgs.toTypedArray()
        )

        if (cursor.moveToFirst()) {
            val firstName = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
            val lastName = cursor.getString(cursor.getColumnIndex(LAST_NAME))
            val email = cursor.getString(cursor.getColumnIndex(EMAIL))
            val age = cursor.getInt(cursor.getColumnIndex(AGE))
            val gender = cursor.getString(cursor.getColumnIndex(GENDER))
            val date = cursor.getString(cursor.getColumnIndex(BIRTH_DATE))

            text = "Name : $firstName $lastName\nEmail : $email\nAge : $age\nGender : $gender\nBirth Date : $date"
            cursor.close()
        }

        return text
    }

    @SuppressLint("Range")
    suspend fun isUserExists(email: String, password: String): Boolean {
        val selectionArgs = listOf<String>(email, password)
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $EMAIL= ? and $PASSWORD = ?",
            selectionArgs.toTypedArray()
        )
        return cursor.count > 0
    }

    companion object {
        private const val DATABASE_NAME = "USER_DETAILS"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "user"
        const val FIRST_NAME = "firstname"
        const val LAST_NAME = "lastname"
        const val EMAIL = "email"
        const val GENDER = "gender"
        const val BIRTH_DATE = "birthdate"
        const val PASSWORD = "password"
        const val AGE = "age"
    }
}
