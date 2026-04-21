package com.example.nasacosmosmessenger

/**
 * 訊息資料模型
 */
data class Message(
    val content: String,
    val isUser: Boolean,               // true 代表使用者，false 代表 Nova
    val apodData: ApodResponse? = null, // 如果這則訊息包含天文圖，就存在這裡
    val videoTitle: String? = null,
    val videoDesc: String? = null
)