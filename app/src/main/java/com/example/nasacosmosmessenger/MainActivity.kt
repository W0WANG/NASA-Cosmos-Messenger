package com.example.nasacosmosmessenger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nasacosmosmessenger.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 測試按鈕點擊
        binding.buttonSend.setOnClickListener {
            val input = binding.editTextMessage.text.toString()
            if (input.isNotBlank()) {
                // 這裡之後要寫傳送邏輯
                binding.editTextMessage.text.clear()
            }
        }
    }
}