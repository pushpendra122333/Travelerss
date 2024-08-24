package com.example.traveler

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        dbHelper = DatabaseHelper(this)
        car = intent.getParcelableExtra("car") ?: throw IllegalArgumentException("Car data missing")


        vehicleImage = findViewById(R.id.vehicle_image)
        vehicleName = findViewById(R.id.vehicle_name)
        vehiclePrice = findViewById(R.id.vehicle_price)
        daysEditText = findViewById(R.id.days_input)
        totalAmountTextView = findViewById(R.id.total_amount)
        bookingDetails = findViewById(R.id.booking_details)
        confirmButton = findViewById(R.id.confirm_booking_button)
        calculateButton = findViewById(R.id.calculate_button)
        // Set initial data
        vehicleImage.setImageResource(car.imageResource)
        vehicleName.text = car.name
        vehiclePrice.text = car.price
        bookingDetails.text = "Daily rate: ${car.price}"
        calculateButton.setOnClickListener {
            try {
                val days = daysEditText.text.toString().toIntOrNull()
                if (days != null && days > 0) {
                    val price = car.price.removePrefix("₹ ").removeSuffix(" / Per Day").replace(",", "").toIntOrNull()
                    if (price != null) {
                        val amount = price * days
                        totalAmountTextView.text = "Total amount: ₹ $amount"
                    } else {
                        totalAmountTextView.text = "Invalid price format"
                    }
                } else {
                    totalAmountTextView.text = "Please enter a valid number of days"
                }
            } catch (e: Exception) {
                // Log the error to understand the cause of the crash
                e.printStackTrace()
                totalAmountTextView.text = "An error occurred. Please try again."
            }
        }
        confirmButton.setOnClickListener {
            val days = daysEditText.text.toString().toIntOrNull()
            if (days != null && days > 0) {
                val perDayPrice = car.price.removePrefix("₹ ").removeSuffix(" / Per Day").replace(",", "").toInt()
                val totalPrice = days * perDayPrice

                val bookingTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                val userId = getCurrentUserId()

                // Debug log for userId
                Log.d("BookingActivity", "User ID: $userId")

                // Save booking to database
                val result = dbHelper.insertBooking(car.name, days, "₹ $totalPrice", userId, bookingTime, 0)

                // Debug log for result
                Log.d("BookingActivity", "Insert result: $result")

                if (result != -1L) {
                    // Booking was successfully inserted
                    totalAmountTextView.text = "Total Amount: ₹ $totalPrice"
                    Toast.makeText(this, "Booking successfully placed!", Toast.LENGTH_SHORT).show()
                    // Navigate to OrderFragment or finish activity
                    // For example: finish()
                } else {
                    // Failed to insert booking
                    Toast.makeText(this, "Failed to place booking. Please try again.", Toast.LENGTH_SHORT).show()
                }
            } else {
                totalAmountTextView.text = "Please enter a valid number of days"
            }
        }

    }


    private fun parsePrice(price: String): Int {
        return price.replace("₹ ", "").replace("/ Per Day", "").replace(",", "").toInt()
    }


    private fun getCurrentUserId(): Int {
        // Replace with actual logic to get the current user's ID
        return 1 // Example user ID
    }


}
