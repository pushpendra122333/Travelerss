package com.example.traveler

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val videoView: VideoView = findViewById(R.id.videoView)
        val videoPath = "android.resource://" + packageName + "/" + R.raw.demo

        val uri = Uri.parse(videoPath)
        videoView.setVideoURI(uri)


        videoView.start()

        // Loop the video infinitely
        videoView.setOnCompletionListener {
            videoView.start() // Restart the video when it reaches the end
        }
videoView.setOnClickListener{
    val intent = Intent(this, loginpage::class.java)
    startActivity(intent)
}
    }
}