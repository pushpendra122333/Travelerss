package com.example.traveler

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

// BookingActivity.kt
class BookingActivity : AppCompatActivity() {

    private lateinit var startdate10:String
    private lateinit var enddate10:String
    private lateinit var addresses:String

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var car: Car
    private lateinit var vehicleImage: ImageView
    private lateinit var vehicleName: TextView
    private lateinit var vehiclePrice: TextView
    private lateinit var bookingDetails: TextView
    private lateinit var daysEditText: TextView
    private lateinit var totalAmountTextView: TextView
    private lateinit var confirmButton: TextView
    private lateinit var calculateButton: TextView
    private lateinit var loginManager: LoginManager
    private lateinit var start_date_input: TextView
    private lateinit var end_date_input: TextView
    private lateinit var addressEditText: EditText

    var startDate: Calendar? = null
    var endDate: Calendar? = null


    @SuppressLint("ClickableViewAccessibility")
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
        start_date_input = findViewById(R.id.start_date_input)
        end_date_input = findViewById(R.id.end_date_input)
        addressEditText=findViewById(R.id.addressEditText)
        start_date_input.setOnClickListener {
            showDatePicker { date ->
                startDate = date
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.time)
              startdate10= formattedDate
                start_date_input.text = formattedDate

                Log.d("DatePicker", "Selected Start Date: $formattedDate")

                // Calculate days if both startDate and endDate are selected
                if (startDate != null && endDate != null) {
                    calculateDaysAndPrice()
                }
            }
        }

        end_date_input.setOnClickListener {
            showDatePicker { date ->
                endDate = date
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.time)
                end_date_input.text = formattedDate
                enddate10=formattedDate
                Log.d("DatePicker", "Selected End Date: $formattedDate")

                // Calculate days if both startDate and endDate are selected
                if (startDate != null && endDate != null) {
                    calculateDaysAndPrice()
                }
            }
        }

        // Set initial data
        vehicleImage.setImageResource(car.imageResource)
        vehicleName.text = car.name
        vehiclePrice.text = car.price
        bookingDetails.text = "Daily rate: ${car.price}"


        confirmButton.setOnClickListener {
            val days = daysEditText.text.toString().toIntOrNull()
            val address = addressEditText.text.toString()
             addresses = address
            when {
                // Check if the number of days is valid
                days == null || days <= 0 -> {
                    Toast.makeText(this, "Please enter a valid number of days.", Toast.LENGTH_SHORT).show()
                }
                // Check if the number of days exceeds the limit
                days > 28 -> {
                    Toast.makeText(this, "You can book for a maximum of 28 days.", Toast.LENGTH_LONG).show()
                }
                // Check if the address field is empty
                address.isEmpty() -> {
                    Toast.makeText(this, "Please enter your address.", Toast.LENGTH_SHORT).show()
                }
                else -> {
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
                    intent.putExtra("startDate", start_date_input.toString()) // Pass start date string
                    intent.putExtra("endDate", end_date_input.toString())
                    intent.putExtra("address", addressEditText.toString())
                    startActivityForResult(intent, PAYMENT_REQUEST_CODE)
                }
            }
        }

    }
    private fun showDatePicker(onDateSelected: (Calendar) -> Unit) {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth)
                set(Calendar.DAY_OF_MONTH, selectedDay)
            }
            onDateSelected(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    // Function to calculate the number of days between start and end date
    private fun calculateDaysAndPrice() {
        val daysBetween = ((endDate!!.timeInMillis - startDate!!.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()

        if (daysBetween >= 0) {
            daysEditText.setText(daysBetween.toString())
            updateTotalAmount(daysBetween)
        } else {
            Toast.makeText(this, "End date must be after start date.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateTotalAmount(days: Int) {
        val perDayPrice = car.price.removePrefix("₹ ").removeSuffix(" / Per Day").replace(",", "").toInt()
        val totalPrice = days * perDayPrice
        totalAmountTextView.text = "Total Amount: ₹ $totalPrice"
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
                val result = dbHelper.insertBooking(carName ?: "",
                    days, "₹ $totalPrice", userId, bookingTime, 0,0.0,startdate10,enddate10,addresses )

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
