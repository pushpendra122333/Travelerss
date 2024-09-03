package com.example.traveler

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class AdminPage : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var userListContainer: LinearLayout
    private lateinit var button: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_page)

        dbHelper = DatabaseHelper(this)
        button = findViewById(R.id.adminbtn)
        userListContainer = findViewById(R.id.ul1)
        displayData()

        button.setOnClickListener {
            val intent = Intent(this, loginpage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun displayData() {
        val cursor = dbHelper.getAllUsers()

        if (cursor != null && cursor.moveToFirst()) {
            userListContainer.removeAllViews()

            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME))
                val email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL))
                val banned = dbHelper.isUserBanned(email) // Check if the user is banned

                val userView = LayoutInflater.from(this).inflate(R.layout.user_item, null)

                val userTextView: TextView = userView.findViewById(R.id.userTextView)
                val banButton: Button = userView.findViewById(R.id.banButton)
                val deleteButton: Button = userView.findViewById(R.id.deleteButton)
                val unbanButton: Button = userView.findViewById(R.id.unbanButton)

                userTextView.text = "ID: $id\nName: $name\nEmail: $email\nStatus: ${if (banned) "Banned" else "Active"}"

                // Handle Ban Button
                banButton.setOnClickListener {
                    if (!banned) {
                        banUser(email)
                    } else {
                        Toast.makeText(this, "User is already banned.", Toast.LENGTH_SHORT).show()
                    }
                }

                // Handle Unban Button
                unbanButton.setOnClickListener {
                    if (banned) {
                        unbanUser(email)
                    } else {
                        Toast.makeText(this, "User is not banned.", Toast.LENGTH_SHORT).show()
                    }
                }

                // Handle Delete Button
                deleteButton.setOnClickListener {
                    deleteUser(email)
                }

                // Toggle visibility based on user's ban status
                if (banned) {
                    unbanButton.visibility = Button.VISIBLE
                    banButton.visibility = Button.GONE
                } else {
                    unbanButton.visibility = Button.GONE
                    banButton.visibility = Button.VISIBLE
                }

                userListContainer.addView(userView)

            } while (cursor.moveToNext())

            cursor.close()
        } else {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun banUser(email: String) {
        val rowsAffected = dbHelper.banUser(email)
        if (rowsAffected > 0) {
            Toast.makeText(this, "User banned successfully!", Toast.LENGTH_SHORT).show()
            displayData() // Refresh the user list
        } else {
            Toast.makeText(this, "Failed to ban user.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun unbanUser(email: String) {
        val rowsAffected = dbHelper.unbanUser(email)
        if (rowsAffected > 0) {
            Toast.makeText(this, "User unbanned successfully!", Toast.LENGTH_SHORT).show()
            displayData() // Refresh the user list
        } else {
            Toast.makeText(this, "Failed to unban user.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteUser(email: String) {
        val rowsDeleted = dbHelper.deleteUser(email)
        if (rowsDeleted > 0) {
            Toast.makeText(this, "User deleted successfully!", Toast.LENGTH_SHORT).show()
            displayData() // Refresh the user list
        } else {
            Toast.makeText(this, "Failed to delete user.", Toast.LENGTH_SHORT).show()
        }
    }
}
