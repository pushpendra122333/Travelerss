package com.example.traveler

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "users.db"
        private const val DATABASE_VERSION = 4 // Increment version for schema update
        private const val TABLE_USERS = "users"
        private const val TABLE_BOOKINGS = "bookings"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_BANNED = "banned" // New column for banned status

        const val COLUMN_BOOKING_ID = "booking_id"
        const val COLUMN_VEHICLE_NAME = "vehicle_name"
        const val COLUMN_DAYS = "days"
        const val COLUMN_TOTAL_AMOUNT = "total_amount"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_BOOKING_TIME = "booking_time"
        const val COLUMN_RETURNED = "returned"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_EMAIL TEXT,"
                + "$COLUMN_PASSWORD TEXT,"
                + "$COLUMN_BANNED INTEGER DEFAULT 0" // Default to not banned
                + ")")

        val createBookingsTable = ("CREATE TABLE $TABLE_BOOKINGS ("
                + "$COLUMN_BOOKING_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_VEHICLE_NAME TEXT,"
                + "$COLUMN_DAYS INTEGER,"
                + "$COLUMN_TOTAL_AMOUNT TEXT,"
                + "$COLUMN_USER_ID INTEGER,"
                + "$COLUMN_BOOKING_TIME TEXT,"
                + "$COLUMN_RETURNED INTEGER DEFAULT 0,"
                + "FOREIGN KEY($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID)"
                + ")")


        db?.execSQL(createTable)
        db?.execSQL(createBookingsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 4) {
            db?.execSQL("ALTER TABLE $TABLE_USERS ADD COLUMN $COLUMN_BANNED INTEGER DEFAULT 0")
            db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_BOOKINGS ("
                    + "$COLUMN_BOOKING_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "$COLUMN_VEHICLE_NAME TEXT,"
                    + "$COLUMN_DAYS INTEGER,"
                    + "$COLUMN_TOTAL_AMOUNT TEXT,"
                    + "$COLUMN_USER_ID INTEGER,"
                    + "$COLUMN_BOOKING_TIME TEXT,"
                    + "$COLUMN_RETURNED INTEGER DEFAULT 0,"
                    + "FOREIGN KEY($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID)"
                    + ")")
        }
    }

    fun getUserByEmail(email: String): Cursor {
        val db = this.readableDatabase
        return db.query(
            TABLE_USERS,
            arrayOf(COLUMN_NAME, COLUMN_EMAIL), // Specify the columns you need
            "$COLUMN_EMAIL = ?",
            arrayOf(email),
            null,
            null,
            null
        )
    }

    // Function to insert new user
    fun insertUser(name: String, email: String, password: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, name)
        contentValues.put(COLUMN_EMAIL, email)
        contentValues.put(COLUMN_PASSWORD, password)
        contentValues.put(COLUMN_BANNED, 0) // Default to not banned

        return db.insert(TABLE_USERS, null, contentValues)
    }

    // Function to check user credentials
    fun checkUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID),
            "$COLUMN_EMAIL=? AND $COLUMN_PASSWORD=? AND $COLUMN_BANNED=0",
            arrayOf(email, password),
            null,
            null,
            null
        )
        val cursorCount = cursor.count
        cursor.close()
        db.close()

        return cursorCount > 0
    }

    // Insert booking data
    fun insertBooking(
        vehicleName: String,
        days: Int?,
        totalAmount: String,
        userId: Int,
        bookingTime: String,
        i: Int
    ): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_VEHICLE_NAME, vehicleName)
            put(COLUMN_DAYS, days)
            put(COLUMN_TOTAL_AMOUNT, totalAmount)
            put(COLUMN_USER_ID, userId)
            put(COLUMN_BOOKING_TIME, bookingTime)
            put(COLUMN_RETURNED, 0) // Initially not returned
        }

        Log.d("DatabaseHelper", "Inserting booking with values: $contentValues")

        val result = db.insert(TABLE_BOOKINGS, null, contentValues)

        Log.d("DatabaseHelper", "Insert result: $result")

        return result
    }




    @SuppressLint("Range")
    fun getAllBookingsForUser(userId: Int): List<Booking> {
        val bookings = mutableListOf<Booking>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_BOOKINGS WHERE $COLUMN_USER_ID = ?", arrayOf(userId.toString()))
        Log.d("DatabaseHelper", "Query: SELECT * FROM $TABLE_BOOKINGS WHERE $COLUMN_USER_ID = $userId")

        if (cursor.moveToFirst()) {
            do {
                val vehicleName = cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_NAME))
                val days = cursor.getInt(cursor.getColumnIndex(COLUMN_DAYS))
                val totalAmount = cursor.getString(cursor.getColumnIndex(COLUMN_TOTAL_AMOUNT))
                val bookingTime = cursor.getString(cursor.getColumnIndex(COLUMN_BOOKING_TIME))
                val returned = cursor.getInt(cursor.getColumnIndex(COLUMN_RETURNED)) == 1

                bookings.add(Booking(vehicleName, days, totalAmount, bookingTime, returned))
            } while (cursor.moveToNext())
        } else {
            Log.d("DatabaseHelper", "No bookings found for user ID: $userId")
        }
        cursor.close()
        return bookings
    }



    // Function to get all users
    fun getAllUsers(): Cursor {
        val db = this.readableDatabase
        return db.query(
            TABLE_USERS,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }


    // Function to ban a user
    fun banUser(email: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_BANNED, 1) // Set banned to true

        return db.update(
            TABLE_USERS,
            contentValues,
            "$COLUMN_EMAIL = ?",
            arrayOf(email)
        )
    }

    // Function to unban a user
    fun unbanUser(email: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_BANNED, 0) // Set banned to false

        return db.update(
            TABLE_USERS,
            contentValues,
            "$COLUMN_EMAIL = ?",
            arrayOf(email)
        )
    }

    // Function to delete a user
    fun deleteUser(email: String): Int {
        val db = this.writableDatabase
        return db.delete(
            TABLE_USERS,
            "$COLUMN_EMAIL = ?",
            arrayOf(email)
        )
    }

    // Function to check if a user is banned
    @SuppressLint("Range")
    fun isUserBanned(email: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_BANNED),
            "$COLUMN_EMAIL = ?",
            arrayOf(email),
            null,
            null,
            null
        )
        val banned = if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndex(COLUMN_BANNED)) == 1
        } else {
            false
        }
        cursor.close()
        db.close()
        return banned
    }

    // Function to check admin credentials
    fun checkAdmin(email: String, password: String): Boolean {
        // Replace with actual admin credentials or use a more secure approach
        val adminEmail = "Boss@gmail.com"
        val adminPassword = "ps123"

        return email == adminEmail && password == adminPassword
    }
}