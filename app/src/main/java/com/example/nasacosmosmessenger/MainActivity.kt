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

        //初始化 UI
        recyclerView = findViewById(R.id.recyclerView)
        editTextMessage = findViewById(R.id.editTextMessage)
        buttonSend = findViewById(R.id.buttonSend)

        //設定 RecyclerView
        adapter = MessageAdapter(messageList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        addMessage(Message("您好，助理!", isUser = true))
        addMessage(Message("歡迎!輸入日期我會告訴你那天宇宙長什麼樣子。", isUser = false))
        //Nova 主動打招呼
        //addMessage(Message("你好！我是 Nova。輸入日期 (YYYY-MM-DD) 讓我為你導覽宇宙吧！", false))

        //Nova 主動抓取今天的 APOD
        val today = java.time.LocalDate.now().toString() // 取得今日日期 (YYYY-MM-DD)
        fetchTodayApod(today)

        //設定傳送按鈕
        buttonSend.setOnClickListener {
            val input = editTextMessage.text.toString().trim()
            if (input.isNotBlank()) {
                // 使用者發送訊息
                addMessage(Message(input, true))

                // 判斷是否為日期格式 (簡單判斷)
                val normalizedInput = input.replace("/", "-")  // 統一轉成 YYYY-MM-DD

                if (normalizedInput.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
                    fetchNasaData(normalizedInput)
                } else {
                    addMessage(Message("請輸入正確的日期格式，例如：2026-04-18 或 2026/04/18", false))
                }

                editTextMessage.text.clear()
            }
        }

        val bottomNav = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_nova -> true
                R.id.nav_favorites -> {
                    startActivity(android.content.Intent(this, FavoritesActivity::class.java))
                    true
                }
                else -> false
            }
        }

        val buttonDatePicker = findViewById<ImageButton>(R.id.buttonDatePicker)
        buttonDatePicker.setOnClickListener {
            val datePicker = android.app.DatePickerDialog(this)
            datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
                // month 從 0 開始所以要 +1
                val date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                editTextMessage.setText(date)
            }
            datePicker.show()
        }
    }

    private fun addMessage(message: Message) {
        messageList.add(message)
        adapter.notifyItemInserted(messageList.size - 1)
        recyclerView.scrollToPosition(messageList.size - 1)
    }

    // 建立一個專門給「今天圖案」用的函數，讓文字不一樣
    private fun fetchTodayApod(date: String) {
        lifecycleScope.launch {
            try {
                val data = RetrofitClient.nasaService.getApod(date)
                if (data.mediaType == "video") {
                    // 影片沒有卡片，文字顯示連結
                    addMessage(Message("這是今天的 APOD：${data.title}\n🎬 影片連結：${data.url}", isUser = false))
                } else {
                    // 圖片用卡片顯示
                    addMessage(Message("這是今天的 APOD：", isUser = false, data))
                }
//                val text = if (data.mediaType == "video") {
//                    "這是今天的 APOD：${data.title}\n📅 ${data.date}\n\n${data.explanation}\n\n🎬 影片連結：${data.url}"
//                } else {
//                    "這是今天的 APOD：${data.title}\n📅 ${data.date}\n\n${data.explanation}"
//                }
//                val apodArg = if (data.mediaType == "image") data else null
//                addMessage(Message(text, isUser = false, apodArg))
            } catch (e: Exception) {
                addMessage(Message("抱歉，我暫時抓不到今天的天象...", isUser = false))
            }
        }
    }

    private fun fetchNasaData(date: String) {
        lifecycleScope.launch {
            try {
                val data = RetrofitClient.nasaService.getApod(date)
                if (data.mediaType == "video") {
                    addMessage(Message("這是 ${data.date} 的宇宙：${data.title}\n🎬 影片連結：${data.url}", false))
                } else {
                    addMessage(Message("這是 ${data.date} 的宇宙：", false, data))
                }
//                val text = if (data.mediaType == "video") {
//                    "這是 ${data.date} 的宇宙：${data.title}\n\n${data.explanation}\n\n🎬 影片連結：${data.url}"
//                } else {
//                    "這是 ${data.date} 的宇宙：${data.title}\n\n${data.explanation}"
//                }
//                val apodArg = if (data.mediaType == "image") data else null
//                addMessage(Message(text, false, apodArg))
            } catch (e: Exception) {
                addMessage(Message("錯誤：${e.message}", false))
            }
        }
    }
}