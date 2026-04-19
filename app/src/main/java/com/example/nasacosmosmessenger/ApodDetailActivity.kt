package com.example.nasacosmosmessenger

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load

class ApodDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apod_detail)

        // 從 Intent 取得資料
        val title = intent.getStringExtra("title") ?: ""
        val date = intent.getStringExtra("date") ?: ""
        val explanation = intent.getStringExtra("explanation") ?: ""
        val url = intent.getStringExtra("url") ?: ""

        findViewById<TextView>(R.id.textDetailTitle).text = title
        findViewById<TextView>(R.id.textDetailDate).text = date
        findViewById<TextView>(R.id.textDetailExplanation).text = explanation
        findViewById<ImageView>(R.id.imageDetail).load(url) { crossfade(true) }
    }
}