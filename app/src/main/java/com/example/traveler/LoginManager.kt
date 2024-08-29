package com.example.traveler

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class LoginManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    // Function to store logged-in email
    fun storeLoggedInEmail(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("loggedInEmail", email)
        editor.apply()
    }

    // Function to get logged-in email
    fun getLoggedInEmail(): String? {
        return sharedPreferences.getString("loggedInEmail", null)
    }

    // Function to store logged-in username
    fun storeLoggedInUserName(name: String) {
        val editor = sharedPreferences.edit()
        editor.putString("loggedInUserName", name)
        editor.apply()
    }

    // Function to get logged-in username
    fun getLoggedInUserName(): String? {
        return sharedPreferences.getString("loggedInUserName", null)
    }

    // Function to store logged-in user ID
    fun storeLoggedInUserId(userId: String) {
        Log.d("LoginManager", "Storing userId: $userId") // Add this line
        val editor = sharedPreferences.edit()
        editor.putString("loggedInUserId", userId)
        editor.apply()
    }

    fun getLoggedInUserId(): String? {
        val userId = sharedPreferences.getString("loggedInUserId", null)
        Log.d("LoginManager", "Retrieved userId: $userId") // Add this line
        return userId
    }

    // Function to log out the user
    fun logout() {
        val editor = sharedPreferences.edit()
        editor.remove("loggedInEmail")
        editor.remove("loggedInUserName")
        editor.remove("loggedInUserId")
        editor.apply()
    }

    // Function to check if user is logged in
    fun isLoggedIn(): Boolean {
        return getLoggedInEmail() != null
    }
}
