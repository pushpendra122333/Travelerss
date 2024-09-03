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

class ResetPassword : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var emailOrPhone:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_password)

        databaseHelper = DatabaseHelper(this)

        emailOrPhone = intent.getStringExtra("EMAIL_OR_PHONE") ?: ""

        val newPasswordEditText: EditText = findViewById(R.id.etNewPassword)
        val confirmPasswordEditText: EditText = findViewById(R.id.etConfirmPassword)
        val resetPasswordButton: TextView = findViewById(R.id.btnResetPassword)

        resetPasswordButton.setOnClickListener {
            val newPassword = newPasswordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in both password fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val status = databaseHelper.updatePassword(emailOrPhone, newPassword)

            when (status) {
                -1 -> {
                    Toast.makeText(this, "This is your old password. Please choose a new one.", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    Toast.makeText(this, "Password reset successfully.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, loginpage::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                0 -> {
                    Toast.makeText(this, "Failed to reset password.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}