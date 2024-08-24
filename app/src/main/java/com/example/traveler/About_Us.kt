package com.example.traveler

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class About_Us : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        val about1: TextView=findViewById(R.id.about2)
        about1.setOnClickListener{
            val i = Intent(this,TermsAndCondition::class.java)
            startActivity(i)
        }
    }

}