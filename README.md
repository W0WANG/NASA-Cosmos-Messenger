# 🌌 NASA Cosmos Messenger

一款以對話方式探索宇宙的 Android App，透過 NASA APOD API 讓使用者查詢任意日期的天文圖片。

---

## 專案架構

```
app/src/main/java/com/example/nasacosmosmessenger/
├── MainActivity.kt         # 聊天介面主頁
├── FavoritesActivity.kt    # 收藏頁面
├── ApodDetailActivity.kt   # 天文圖詳細頁（含星空卡分享）
├── MessageAdapter.kt       # 聊天 RecyclerView Adapter
├── FavoritesAdapter.kt     # 收藏 RecyclerView Adapter
├── FavoritesManager.kt     # SharedPreferences 本地收藏管理
├── RetrofitClient.kt       # Retrofit 網路客戶端
├── NasaApiService.kt       # NASA APOD API 介面定義
├── ApodResponse.kt         # API 回傳資料模型
└── Message.kt              # 聊天訊息資料模型
```

---

## 使用技術與選擇原因

| 技術 | 用途 | 選擇原因 |
|------|------|----------|
| **Retrofit** | NASA API 網路請求 | Android 主流 HTTP 客戶端，搭配 Gson 可直接將 JSON 轉成 data class |
| **Coil** | 圖片載入與快取 | 專為 Kotlin 設計，支援協程，語法簡潔 |
| **SharedPreferences** | 本地收藏儲存 | 輕量、無需額外依賴，適合儲存簡單的 JSON 資料 |
| **RecyclerView** | 聊天列表與收藏列表 | 效能佳，支援動態新增訊息 |
| **Lifecyclescope + Coroutines** | 非同步 API 呼叫 | 避免在主執行緒執行網路請求，與 Activity 生命週期綁定 |
| **BottomNavigationView** | Nova / 收藏 Tab 切換 | Material Design 標準元件，使用者體驗直覺 |

---

## 支援的日期格式

| 格式 | 範例 |
|------|------|
| `YYYY-MM-DD` | `2026-04-20` |
| `YYYY/MM/DD` | `2026/04/20` |

> APOD 資料最早始於 **1995-06-16**，輸入早於此日期可能無法取得資料。

---

## 核心功能

- 🤖 **Nova 聊天助理** — 開啟 App 自動顯示今日天文圖，輸入日期查詢指定日天文圖
- 🖼️ **APOD 卡片** — 顯示圖片、標題、說明文字
- ⭐ **長按收藏** — 在聊天室長按卡片加入收藏
- 📂 **收藏頁面** — 獨立 Tab 瀏覽、刪除收藏的天文圖
- 🔍 **詳細頁面** — 點擊卡片查看完整說明文字
- 🎬 **影片支援** — 當日天象為影片時顯示可點擊連結
- 📅 **日期選擇器** — 點擊行事曆圖示快速選擇日期

---

## Bonus 功能

### 🌟 分享星空卡
在詳細頁點擊圖片，App 會將 APOD 圖片、標題、日期合成一張「星空卡」並開啟系統分享介面，可分享至任意社群平台或儲存至相簿。

---

## 環境設定

1. 至 [https://api.nasa.gov/](https://api.nasa.gov/) 申請免費 API Key
2. 在專案根目錄的 `local.properties` 加入：

```
NASA_API_KEY=你的API金鑰
```

> 測試用可使用 `DEMO_KEY`，但每天限制 30 次請求。
