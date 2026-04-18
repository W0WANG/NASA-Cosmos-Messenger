package com.example.nasacosmosmessenger

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextMessage: EditText
    //private lateinit var buttonSend: Button
    private lateinit var buttonSend: ImageButton

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

        addMessage(Message("您好，助理!", isUser = true))
        addMessage(Message("歡迎!輸入日期我會告訴你那天宇宙長什麼樣子。", isUser = false))
        // 3. Nova 主動打招呼
        //addMessage(Message("你好！我是 Nova。輸入日期 (YYYY-MM-DD) 讓我為你導覽宇宙吧！", false))

        // 4. Nova 主動抓取今天的 APOD
        val today = java.time.LocalDate.now().toString() // 取得今日日期 (YYYY-MM-DD)
        fetchTodayApod(today)

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

    // 建立一個專門給「今天圖案」用的函數，讓文字不一樣
    private fun fetchTodayApod(date: String) {
        lifecycleScope.launch {
            try {
                val data = RetrofitClient.nasaService.getApod(date)
                // 這裡讓機器人說出你要求的文字
                addMessage(Message("這是今天的 APOD：${data.title}", isUser = false, data))
            } catch (e: Exception) {
                // 如果今天還沒更新或網路出錯
                addMessage(Message("抱歉，我暫時抓不到今天的天象...", isUser = false))
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
                addMessage(Message("錯誤：${e.message}", false))  // ← 暫時改成這樣除錯
            }
        }
    }
}