package com.example.traveler

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

// BookingActivity.kt
class BookingActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var car: Car
    private lateinit var vehicleImage: ImageView
    private lateinit var vehicleName: TextView
    private lateinit var vehiclePrice: TextView
    private lateinit var bookingDetails: TextView
    private lateinit var daysEditText: EditText
    private lateinit var totalAmountTextView: TextView
    private lateinit var confirmButton: TextView
    private lateinit var calculateButton: TextView
    private lateinit var loginManager: LoginManager

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        dbHelper = DatabaseHelper(this)
        loginManager = LoginManager(this)

        car = intent.getParcelableExtra("car") ?: throw IllegalArgumentException("Car data missing")


        vehicleImage = findViewById(R.id.vehicle_image)
        vehicleName = findViewById(R.id.vehicle_name)
        vehiclePrice = findViewById(R.id.vehicle_price)
        daysEditText = findViewById(R.id.days_input)
        totalAmountTextView = findViewById(R.id.total_amount)
        bookingDetails = findViewById(R.id.booking_details)
        confirmButton = findViewById(R.id.confirm_booking_button)

        // Set initial data
        vehicleImage.setImageResource(car.imageResource)
        vehicleName.text = car.name
        vehiclePrice.text = car.price
        bookingDetails.text = "Daily rate: ${car.price}"
        confirmButton.setOnClickListener {
            val days = daysEditText.text.toString().toIntOrNull()
            if (days != null && days > 0) {
                if (days > 28) {
                    // Show a message if the number of days exceeds 30
                    Toast.makeText(this, "You can book for a maximum of 28 days.", Toast.LENGTH_LONG).show()
                } else {
                    val perDayPrice = car.price.removePrefix("₹ ").removeSuffix(" / Per Day").replace(",", "").toInt()
                    val totalPrice = days * perDayPrice

                    val userId = getCurrentUserId()
                    Log.d("BookingActivity", "User ID before booking: $userId")

                    // Redirect to PaymentActivity instead of directly saving the booking
                    val intent = Intent(this, PaymentActivity::class.java)
                    intent.putExtra("carName", car.name)
                    intent.putExtra("days", days)
                    intent.putExtra("totalAmount", totalPrice)
                    intent.putExtra("userId", getCurrentUserId())
                    intent.putExtra("bookingTime", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))

                    startActivityForResult(intent, PAYMENT_REQUEST_CODE)
                }
            } else {
                totalAmountTextView.text = "Please enter a valid number of days"
            }
        }

    }
    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYMENT_REQUEST_CODE && resultCode == RESULT_OK) {

            val paymentStatus = data?.getStringExtra("paymentStatus")
            val paymentMethod = data?.getStringExtra("paymentMethod")
            val carName = data?.getStringExtra("carName")
            val days = data?.getIntExtra("days", 0) ?: 0
            val totalPrice = data?.getIntExtra("totalAmount", 0) ?: 0
            val userId = data?.getIntExtra("userId", 0) ?: 0
            Log.d("PaymentActivity", "User ID received in PaymentActivity: $userId")
            val bookingTime = data?.getStringExtra("bookingTime") ?: ""

            if (paymentStatus == "success" && days > 0) {
                // Save the booking to the database
                val result = dbHelper.insertBooking(carName ?: "", days, "₹ $totalPrice", userId, bookingTime, 0)

                if (result != -1L) {
                    totalAmountTextView.text = "Total Amount: ₹ $totalPrice"
                    Toast.makeText(this, "Booking successfully placed!", Toast.LENGTH_SHORT).show()
                    // Navigate to OrderFragment or finish activity
                    scheduleReturnStatusUpdate(userId, carName, days)
                    // For example: finish()
                } else {
                    Toast.makeText(this, "Failed to place booking. Please try again.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Payment failed or was canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun scheduleReturnStatusUpdate(userId: Int, carName: String?, days: Int) {
        val intent = Intent(this, ReturnStatusUpdateReceiver::class.java).apply {
            putExtra("userId", userId)
            putExtra("carName", carName)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this, userId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerTime = System.currentTimeMillis() + days * 24 * 60 * 60 * 1000L // days to milliseconds

        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }

    companion object {
        private const val PAYMENT_REQUEST_CODE = 1001
    }

    private fun getCurrentUserId(): Int? {
        val userIdString = loginManager.getLoggedInUserId()
        return userIdString?.toIntOrNull()
    }


}
