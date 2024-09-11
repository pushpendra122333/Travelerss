package com.example.traveler

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PaymentActivity : AppCompatActivity() {
    private lateinit var carNameTextView: TextView
    private lateinit var totalAmountTextView: TextView
    private lateinit var payButton: Button
    private lateinit var upiButton: Button
    private lateinit var qrButton: Button
    private lateinit var codButton: Button
    private lateinit var qrCodeImage: ImageView

    private var paymentMethod: String? = null
    private var totalAmount: Int = 0
    private var carName: String? = null
    private var days: Int = 0
    private var userId: Int = 0
    private var bookingTime: String? = null
    private var address:String?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        carNameTextView = findViewById(R.id.car_name)
        totalAmountTextView = findViewById(R.id.total_amount)
        payButton = findViewById(R.id.pay_button)
        upiButton = findViewById(R.id.upi_button)
        qrButton = findViewById(R.id.qr_button)
        codButton = findViewById(R.id.cod_button)
        qrCodeImage = findViewById(R.id.qr_code_image)

        val carNames = intent.getStringExtra("carName")
        totalAmount = intent.getIntExtra("totalAmount", 0)

        // Set the car name and total amount in the UI
        carNameTextView.text = carNames
        totalAmountTextView.text = "Total Amount: ₹ $totalAmount"

        // Retrieve data from the intent
        carName = intent.getStringExtra("carName")
        totalAmount = intent.getIntExtra("totalAmount", 0)
        days = intent.getIntExtra("days", 0)
        userId = intent.getIntExtra("userId", 0)
        bookingTime = intent.getStringExtra("bookingTime")
        address = intent.getStringExtra("address")
        upiButton.setOnClickListener {
            paymentMethod = "UPI"
            showUpiNumber()
        }

        qrButton.setOnClickListener {
            paymentMethod = "QR"
            showQrCode()
        }

        codButton.setOnClickListener {
            paymentMethod = "COD"
            Toast.makeText(this, "Cash on Delivery selected. Payment will be collected upon delivery.", Toast.LENGTH_LONG).show()
        }

        payButton.setOnClickListener {
            if (paymentMethod != null) {
                // Simulate payment completion for UPI or QR
                confirmPayment()
            } else {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showUpiNumber() {
        // Hide QR Code image and show UPI number
        qrCodeImage.visibility = ImageView.GONE
        findViewById<TextView>(R.id.upi_number_display).apply {
            text = "UPI Number: ps3389401-1@okicici"
            visibility = TextView.VISIBLE
        }
        Toast.makeText(this, "Please pay using the UPI number displayed.", Toast.LENGTH_LONG).show()
    }

    private fun showQrCode() {
        // Hide UPI number and show QR Code image
        findViewById<TextView>(R.id.upi_number_display).visibility = TextView.GONE
        qrCodeImage.apply {
            visibility = ImageView.VISIBLE
            setImageResource(R.drawable.qr_code) // Replace with your QR code image resource
        }
        Toast.makeText(this, "Please scan the QR code and complete the payment.", Toast.LENGTH_LONG).show()
    }

    private fun confirmPayment() {
        // Simulate successful payment process
        val resultIntent = Intent().apply {
            putExtra("paymentStatus", "success")
            putExtra("paymentMethod", paymentMethod)
            putExtra("carName", carName)
            putExtra("days", days)
            putExtra("totalAmount", totalAmount)
            putExtra("userId", userId)
            putExtra("bookingTime", bookingTime)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}
