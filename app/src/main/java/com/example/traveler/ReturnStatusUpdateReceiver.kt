package com.example.traveler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ReturnStatusUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val dbHelper = DatabaseHelper(context)
        val userId = intent.getIntExtra("userId", -1)
        val carName = intent.getStringExtra("carName")

        if (userId != -1 && carName != null) {
            val success = dbHelper.updateReturnStatus(userId, carName, 1) // 1 for Returned
            if (success) {
                Log.d("ReturnStatusUpdate", "Successfully updated return status for $carName")
            } else {
                Log.e("ReturnStatusUpdate", "Failed to update return status for $carName")
            }
        } else {
            Log.e("ReturnStatusUpdate", "Invalid data received in intent")
        }
    }
}