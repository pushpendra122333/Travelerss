package com.example.traveler

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class loginpage : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var loginManager: LoginManager
    private lateinit var passwordEditText: EditText
    private var isPasswordVisible = false


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginpage)

        databaseHelper = DatabaseHelper(this)
        loginManager = LoginManager(this)

        passwordEditText = findViewById(R.id.ET2)

        // Initialize password EditText with eye icon toggle
        passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
            R.drawable.baseline_lock_24,  // Left icon
            0,  // Top
            R.drawable.baseline_visibility_off_24,  // Right icon (initially eye closed)
            0   // Bottom
        )

        // Set a touch listener for the eye icon
        passwordEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2 // Right side drawable
                if (event.rawX >= (passwordEditText.right - passwordEditText.compoundDrawables[drawableEnd].bounds.width())) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }

        // Check if user is already logged in
        if (loginManager.isLoggedIn()) {
            // Redirect to WelcomePage if already logged in
            val intent = Intent(this, WelcomePage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        }


        val emailEditText: EditText = findViewById(R.id.ET1)
        val passwordEditText: EditText = findViewById(R.id.ET2)
        val loginButton: TextView = findViewById(R.id.b2)
        val forgetpassword: TextView = findViewById(R.id.textView6)
        val signUpText: TextView = findViewById(R.id.signup)


        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            when {
                databaseHelper.checkAdmin(email, password) -> {
                    // Redirect to AdminPageActivity
                    val intent = Intent(this, AdminPage::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

                databaseHelper.isUserBanned(email) -> {
                    // Show a toast message if the user is banned
                    Toast.makeText(this, "Your account has been banned.", Toast.LENGTH_SHORT).show()
                }

                databaseHelper.checkUser(email, password) -> {
                    // Retrieve userId from the database
                    val userCursor = databaseHelper.getUserByEmail(email)
                    if (userCursor.moveToFirst()) {
                        val userIdIndex = userCursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ID)
                        val userNameIndex = userCursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)

                        if (userIdIndex != -1 && userNameIndex != -1) {
                            val userId = userCursor.getString(userIdIndex)
                            val userName = userCursor.getString(userNameIndex)

                            // Store user details in LoginManager
                            loginManager.storeLoggedInEmail(email)
                            loginManager.storeLoggedInUserName(userName)
                            loginManager.storeLoggedInUserId(userId)

                            // Redirect to WelcomePage
                            val intent = Intent(this, WelcomePage::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "User details not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                    userCursor.close()
                }

                else -> {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }

        }

        forgetpassword.setOnClickListener {
            val intent = Intent(this, Forget_Password::class.java)
            startActivity(intent)
        }

        signUpText.setOnClickListener {
            val intent = Intent(this, signuppage::class.java)
            startActivity(intent)
        }
    }
    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.baseline_lock_24,  // Left icon
                0,  // Top
                R.drawable.baseline_visibility_off_24,  // Right icon (eye closed)
                0   // Bottom
            )
        } else {
            // Show password
            passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.baseline_lock_24,  // Left icon
                0,  // Top
                R.drawable.baseline_visibility_24,  // Right icon (eye open)
                0   // Bottom
            )
        }
        // Move the cursor to the end of the text
        passwordEditText.setSelection(passwordEditText.text.length)
        isPasswordVisible = !isPasswordVisible
    }
}
