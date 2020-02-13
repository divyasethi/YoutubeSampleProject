package com.youtubesampleproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    var playBtn : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playBtn = findViewById(R.id.playBtn)
        playBtn?.setOnClickListener {

            val intent = Intent(this, Pip::class.java)
            intent.putExtra(Constants.YOUTUBE_ID, "fhWaJi1Hsfo")
            startActivity(intent)

        }
    }
}
