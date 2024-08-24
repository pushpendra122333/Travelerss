package com.example.traveler

import android.content.Context
import android.content.SharedPreferences

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
    fun storeLoggedInUserName(name: String) {
        val editor = sharedPreferences.edit()
        editor.putString("loggedInUserName", name)
        editor.apply()
    }

    // Function to get logged-in username
    fun getLoggedInUserName(): String? {
        return sharedPreferences.getString("loggedInUserName", null)
    }

    // Function to log out the user
    fun logout() {
        val editor = sharedPreferences.edit()
        editor.remove("loggedInEmail")
        editor.apply()
    }

    // Function to check if user is logged in
    fun isLoggedIn(): Boolean {
        return getLoggedInEmail() != null
    }
}
