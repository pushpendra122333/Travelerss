package com.example.traveler

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Forget_Password : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forget_password)

         databaseHelper = DatabaseHelper(this)

        val emailOrPhoneEditText: EditText = findViewById(R.id.etEmailOrPhone)
        val submitButton: TextView= findViewById(R.id.btnSubmit)

        submitButton.setOnClickListener {
            val input = emailOrPhoneEditText.text.toString().trim()

            if (input.isEmpty()) {
                Toast.makeText(this, "Please enter your email or phone number.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (databaseHelper.isEmailOrPhoneExists(input)) {
                // Proceed to reset password activity
                val intent = Intent(this, ResetPassword::class.java)
                intent.putExtra("EMAIL_OR_PHONE", input)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(this, "Email or phone number not found.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}