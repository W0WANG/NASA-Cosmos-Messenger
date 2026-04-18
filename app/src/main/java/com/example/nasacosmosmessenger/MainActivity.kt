package com.example.nasacosmosmessenger

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSend: Button

    private val messageList = mutableListOf<Message>()
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. 初始化 UI
        recyclerView = findViewById(R.id.recyclerView)
        editTextMessage = findViewById(R.id.editTextMessage)
        buttonSend = findViewById(R.id.buttonSend)

        // 2. 設定 RecyclerView
        adapter = MessageAdapter(messageList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 3. Nova 主動打招呼
        addMessage(Message("你好！我是 Nova。輸入日期 (YYYY-MM-DD) 讓我為你導覽宇宙吧！", false))

        // 4. 設定傳送按鈕
        buttonSend.setOnClickListener {
            val input = editTextMessage.text.toString().trim()
            if (input.isNotBlank()) {
                // 使用者發送訊息
                addMessage(Message(input, true))

                // 判斷是否為日期格式 (簡單判斷)
                if (input.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
                    fetchNasaData(input)
                } else {
                    addMessage(Message("請輸入正確的日期格式，例如：2026-04-18", false))
                }

                editTextMessage.text.clear()
            }
        }
    }

    private fun addMessage(message: Message) {
        messageList.add(message)
        adapter.notifyItemInserted(messageList.size - 1)
        recyclerView.scrollToPosition(messageList.size - 1)
    }

    private fun fetchNasaData(date: String) {
        lifecycleScope.launch {
            try {
                val data = RetrofitClient.nasaService.getApod(date)
                addMessage(Message("這是在 $date 拍攝的：${data.title}", false, data))
            } catch (e: Exception) {
                addMessage(Message("抱歉，我找不到那天的宇宙紀錄或連線出錯了...", false))
            }
        }
    }
}