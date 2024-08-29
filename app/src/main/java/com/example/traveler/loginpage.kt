package com.example.traveler

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class loginpage : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var loginManager: LoginManager

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginpage)

        databaseHelper = DatabaseHelper(this)
        loginManager = LoginManager(this)

        // Check if user is already logged in
        if (loginManager.isLoggedIn()) {
            // Redirect to WelcomePage if already logged in
            val intent = Intent(this, WelcomePage::class.java)
            startActivity(intent)
            finish()
            return
        }

        val emailEditText: EditText = findViewById(R.id.ET1)
        val passwordEditText: EditText = findViewById(R.id.ET2)
        val loginButton: TextView = findViewById(R.id.b2)
        val signUpText: TextView = findViewById(R.id.signup)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            when {
                databaseHelper.checkAdmin(email, password) -> {
                    // Redirect to AdminPageActivity
                    val intent = Intent(this, AdminPage::class.java)
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

        signUpText.setOnClickListener {
            val intent = Intent(this, signuppage::class.java)
            startActivity(intent)
        }
    }
}
