package com.example.nasacosmosmessenger

import com.google.gson.annotations.SerializedName

/**
 * NASA 每日天文圖 (APOD) 的 API 回傳模型
 */
data class ApodResponse(
    @SerializedName("date")
    val date: String,           // 日期，格式通常為 YYYY-MM-DD

    @SerializedName("explanation")
    val explanation: String,    // 天文圖的詳細文字介紹 [cite: 58]

    @SerializedName("media_type")
    val mediaType: String,      // 媒體類型："image" 代表圖片，"video" 代表影片

    @SerializedName("title")
    val title: String,          // 天文圖的標題 [cite: 58]

    @SerializedName("url")
    val url: String,            // 圖片網址或影片連結 [cite: 58, 60]

    @SerializedName("hdurl")
    val hdUrl: String? = null   // 高畫質圖片網址（選填）
)