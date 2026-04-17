package com.example.nasacosmosmessenger // 確保與你的專案包名一致

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * NASA API 連線介面
 * 負責定義與 NASA 伺服器溝通的動作
 */
interface NasaApiService {

    /**
     * 取得 NASA 每日天文圖 (APOD)
     * * @param date 指定日期，格式為 YYYY-MM-DD。
     * 若不傳此參數，NASA 預設會回傳「今日」的資料。
     * @return 返回封裝好的 ApodResponse 資料模型
     */
    @GET("planetary/apod")
    suspend fun getApod(
        @Query("date") date: String? = null
    ): ApodResponse
}