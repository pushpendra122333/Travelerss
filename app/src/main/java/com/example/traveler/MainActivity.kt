package com.example.traveler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getStartedButton: TextView = findViewById(R.id.button)
        getStartedButton.setOnClickListener {
            val intent = Intent(this, loginpage::class.java)
            startActivity(intent)
        }
    }
}