package com.example.traveler

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

@Suppress("UNREACHABLE_CODE")
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "users.db"
        private const val DATABASE_VERSION = 8 // Increment version for schema update
        private const val TABLE_USERS = "users"
        private const val TABLE_BOOKINGS = "bookings"

        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PHONE_NUMBER = "phone_number"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_BANNED = "banned" // New column for banned status

        const val COLUMN_BOOKING_ID = "booking_id"
        const val COLUMN_VEHICLE_NAME = "vehicle_name"
        const val COLUMN_START_DATE = "start_date"
        const val COLUMN_END_DATE = "end_date"
        const val COLUMN_DAYS = "days"
        const val COLUMN_TOTAL_AMOUNT = "total_amount"
        const val COLUMN_BOOKING_TIME = "booking_time"
        const val COLUMN_RETURNED = "returned"
        private const val COLUMN_CANCELED = "canceled"
        private const val COLUMN_CANCELLATION_CHARGE = "cancellation_charge"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_EMAIL TEXT,"
                + "$COLUMN_PASSWORD TEXT,"
                + "$COLUMN_PHONE_NUMBER TEXT,"
                + "$COLUMN_BANNED INTEGER DEFAULT 0" // Default to not banned
                + ")")

        val createBookingsTable = ("CREATE TABLE $TABLE_BOOKINGS ("
                + "$COLUMN_BOOKING_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_VEHICLE_NAME TEXT,"
                + "$COLUMN_START_DATE TEXT,"
                + "$COLUMN_END_DATE TEXT,"
                + "$COLUMN_DAYS INTEGER,"
                + "$COLUMN_TOTAL_AMOUNT TEXT,"
                + "$COLUMN_USER_ID INTEGER,"
                + "$COLUMN_BOOKING_TIME TEXT,"
                + "$COLUMN_RETURNED INTEGER DEFAULT 0,"
                + "$COLUMN_CANCELED INTEGER DEFAULT 0,"
                + "$COLUMN_CANCELLATION_CHARGE REAL DEFAULT 0.0,"
                + "FOREIGN KEY($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)"
                + ")")


        db?.execSQL(createTable)
        db?.execSQL(createBookingsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            if (oldVersion < 8) {
                db?.execSQL("ALTER TABLE $TABLE_USERS RENAME TO temp_users")

                db?.execSQL("ALTER TABLE $TABLE_BOOKINGS ADD COLUMN $COLUMN_CANCELED INTEGER DEFAULT 0")
                db?.execSQL("ALTER TABLE $TABLE_BOOKINGS ADD COLUMN $COLUMN_CANCELLATION_CHARGE REAL DEFAULT 0.0")
                // Create new table with updated schema
                db?.execSQL("CREATE TABLE $TABLE_USERS ("
                        + "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "$COLUMN_NAME TEXT,"
                        + "$COLUMN_EMAIL TEXT,"
                        + "$COLUMN_PASSWORD TEXT,"
                        + "$COLUMN_PHONE_NUMBER TEXT,"
                        + "$COLUMN_BANNED INTEGER DEFAULT 0"
                        + ")")

                // Copy data from old table to new table
                db?.execSQL("INSERT INTO $TABLE_USERS ($COLUMN_USER_ID, $COLUMN_NAME, $COLUMN_EMAIL, $COLUMN_PASSWORD, $COLUMN_BANNED)"
                        + " SELECT $COLUMN_USER_ID, $COLUMN_NAME, $COLUMN_EMAIL, $COLUMN_PASSWORD, $COLUMN_BANNED FROM temp_users")

                // Drop the old table
                db?.execSQL("DROP TABLE temp_users")
                db?.execSQL("ALTER TABLE $TABLE_USERS ADD COLUMN $COLUMN_PHONE_NUMBER TEXT")
                // Update foreign key references in bookings table
                db?.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKINGS")
                db?.execSQL("CREATE TABLE $TABLE_BOOKINGS ("
                        + "$COLUMN_BOOKING_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "$COLUMN_VEHICLE_NAME TEXT,"
                        + "$COLUMN_START_DATE TEXT,"
                        + "$COLUMN_END_DATE TEXT,"
                        + "$COLUMN_DAYS INTEGER,"
                        + "$COLUMN_TOTAL_AMOUNT TEXT,"
                        + "$COLUMN_USER_ID INTEGER,"
                        + "$COLUMN_BOOKING_TIME TEXT,"
                        + "$COLUMN_RETURNED INTEGER DEFAULT 0,"
                        + "FOREIGN KEY($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)"
                        + ")")
                db?.execSQL("ALTER TABLE $TABLE_BOOKINGS ADD COLUMN $COLUMN_START_DATE TEXT")
                db?.execSQL("ALTER TABLE $TABLE_BOOKINGS ADD COLUMN $COLUMN_END_DATE TEXT")
            }
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error during database upgrade", e)
        }
    }

    fun getUserByEmail(email: String): Cursor {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?"
        return db.rawQuery(query, arrayOf(email))

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
    fun insertUser(name: String, email: String, password: String,phoneNumber: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, name)
        contentValues.put(COLUMN_EMAIL, email)
        contentValues.put(COLUMN_PASSWORD, password)
        contentValues.put(COLUMN_PHONE_NUMBER, phoneNumber)
        contentValues.put(COLUMN_BANNED, 0) // Default to not banned

        val userId = db.insert(TABLE_USERS, null, contentValues)
        return userId
    }

    // Function to check user credentials
    fun checkUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_USER_ID),
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
        canceled: Int = 0, // Default to not canceled
        cancellationCharge: Double = 0.0
    ): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_VEHICLE_NAME, vehicleName)
            put(COLUMN_DAYS, days)
            put(COLUMN_TOTAL_AMOUNT, totalAmount)
            put(COLUMN_USER_ID, userId)
            put(COLUMN_BOOKING_TIME, bookingTime)
            put(COLUMN_RETURNED, 0) // Initially not returned
            put(COLUMN_CANCELED, canceled)
            put(COLUMN_CANCELLATION_CHARGE, cancellationCharge)
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
                val bookingId = cursor.getInt(cursor.getColumnIndex(COLUMN_BOOKING_ID))
                val vehicleName = cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_NAME))
                val days = cursor.getInt(cursor.getColumnIndex(COLUMN_DAYS))
                val totalAmount = cursor.getString(cursor.getColumnIndex(COLUMN_TOTAL_AMOUNT))
                val bookingTime = cursor.getString(cursor.getColumnIndex(COLUMN_BOOKING_TIME))
                val returned = cursor.getInt(cursor.getColumnIndex(COLUMN_RETURNED)) == 1
                val canceled = cursor.getInt(cursor.getColumnIndex(COLUMN_CANCELED)) == 1
                val cancellationCharge = cursor.getDouble(cursor.getColumnIndex(COLUMN_CANCELLATION_CHARGE))
                bookings.add(Booking(bookingId,vehicleName, days, totalAmount, bookingTime, returned, canceled, cancellationCharge))
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

    fun getUserIdByEmail(email: String): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_USER_ID FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?", arrayOf(email))
        var userId = -1 // Default value for no user found

        try {
            if (cursor.moveToFirst()) {
                // Safely retrieve the index and value
                val columnIndex = cursor.getColumnIndex(COLUMN_USER_ID)
                if (columnIndex != -1) {
                    userId = cursor.getInt(columnIndex)
                    Log.d("DatabaseHelper", "Retrieved user ID: $userId for email: $email")
                } else {
                    Log.e("DatabaseHelper", "Column $COLUMN_USER_ID not found in the cursor")
                }
            } else {
                Log.w("DatabaseHelper", "No user found with email: $email")
            }
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error retrieving user ID for email: $email", e)
        } finally {
            cursor.close()
            db.close()
        }

        return userId
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

    fun updateReturnStatus(userId: Int, carName: String, returned: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("returnStatus", returned)
        }
        val result = db.update("bookings", values, "userId=? AND carName=?", arrayOf(userId.toString(), carName))
        return result > 0
    }

    @SuppressLint("Range")
    fun cancelBooking(bookingId: Int): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_CANCELED, 1) // Mark as canceled
            put(COLUMN_CANCELLATION_CHARGE, calculateCancellationCharge(bookingId)) // Set the cancellation charge
        }
        val result = db.update(TABLE_BOOKINGS, contentValues, "$COLUMN_BOOKING_ID = ?", arrayOf(bookingId.toString()))
        return result > 0
    }
    fun parseTotalAmount(amountString: String): Double {
        val cleanedString = amountString.replace(Regex("[^0-9.]"), "")
        val parsedValue = cleanedString.toDoubleOrNull() ?: 0.0
        Log.d("DatabaseHelper", "Parsed totalAmount: $parsedValue from string: $amountString")
        return parsedValue
    }

    fun calculateCancellationCharge(bookingId: Int): Double {
        val booking = getBookingById(bookingId)
        val totalAmount = parseTotalAmount(booking?.totalAmount ?: "0")
        val charge = totalAmount * 0.03

        Log.d("DatabaseHelper", "Calculated cancellation charge: $charge for booking ID: $bookingId")
        return charge
    }
    @SuppressLint("Range")
    fun getBookingById(bookingId: Int): Booking? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_BOOKINGS,
            null,
            "$COLUMN_BOOKING_ID = ?",
            arrayOf(bookingId.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val vehicleName = cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_NAME))
            val days = cursor.getInt(cursor.getColumnIndex(COLUMN_DAYS))
            val totalAmount = cursor.getString(cursor.getColumnIndex(COLUMN_TOTAL_AMOUNT))
            val bookingTime = cursor.getString(cursor.getColumnIndex(COLUMN_BOOKING_TIME))
            val returned = cursor.getInt(cursor.getColumnIndex(COLUMN_RETURNED)) == 1
            val canceled = cursor.getInt(cursor.getColumnIndex(COLUMN_CANCELED)) == 1
            val cancellationCharge = cursor.getDouble(cursor.getColumnIndex(COLUMN_CANCELLATION_CHARGE))


            Log.d("DatabaseHelper", "Total Amount: $totalAmount") // Debug line

            Booking(bookingId,vehicleName, days, totalAmount, bookingTime, returned, canceled, cancellationCharge)
        } else {
            null
        }.also { cursor.close() }
    }

    fun isEmailExists(email: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM users WHERE email = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
    fun isEmailOrPhoneExists(input: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? OR $COLUMN_PHONE_NUMBER = ?"
        val cursor = db.rawQuery(query, arrayOf(input, input))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
    fun updatePassword(emailOrPhone: String, newPassword: String): Int {
        val db = this.writableDatabase

        // Query to get the old password
        val query = "SELECT $COLUMN_PASSWORD FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? OR $COLUMN_PHONE_NUMBER = ?"
        val cursor = db.rawQuery(query, arrayOf(emailOrPhone, emailOrPhone))

        if (cursor.moveToFirst()) {
            val oldPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            if (newPassword == oldPassword) {
                cursor.close()
                return -1
            }
            val contentValues = ContentValues()
            contentValues.put(COLUMN_PASSWORD, newPassword)

            val rows = db.update(TABLE_USERS, contentValues, "$COLUMN_EMAIL = ? OR $COLUMN_PHONE_NUMBER = ?", arrayOf(emailOrPhone, emailOrPhone))
            cursor.close()
            return if (rows > 0) 1 else 0
        }

        cursor.close()
        return 0
    }

    // Function to check admin credentials
    fun checkAdmin(email: String, password: String): Boolean {
        // Replace with actual admin credentials or use a more secure approach
        val adminEmail = "Boss@gmail.com"
        val adminPassword = "ps123"

        return email == adminEmail && password == adminPassword
    }
}