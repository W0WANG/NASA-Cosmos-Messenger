package com.example.nasacosmosmessenger

import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import coil.load
import coil.request.ImageRequest
import coil.ImageLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ApodDetailActivity : AppCompatActivity() {

    private var loadedBitmap: Bitmap? = null
    private var title = ""
    private var date = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apod_detail)

        title = intent.getStringExtra("title") ?: ""
        date = intent.getStringExtra("date") ?: ""
        val explanation = intent.getStringExtra("explanation") ?: ""
        val url = intent.getStringExtra("url") ?: ""

        findViewById<TextView>(R.id.textDetailTitle).text = title
        findViewById<TextView>(R.id.textDetailDate).text = date
        findViewById<TextView>(R.id.textDetailExplanation).text = explanation

        val imageDetail = findViewById<ImageView>(R.id.imageDetail)

        // 載入圖片並儲存 Bitmap
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val imageLoader = ImageLoader(this@ApodDetailActivity)
                val request = ImageRequest.Builder(this@ApodDetailActivity)
                    .data(url)
                    .allowHardware(false) // 重要！硬體加速的 Bitmap 無法用 Canvas 繪製
                    .build()
                val result = imageLoader.execute(request)
                val drawable = result.drawable

                withContext(Dispatchers.Main) {
                    imageDetail.setImageDrawable(drawable)
                }

                // 轉成 Bitmap
                if (drawable != null) {
                    val bitmap = Bitmap.createBitmap(
                        drawable.intrinsicWidth,
                        drawable.intrinsicHeight,
                        Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(bitmap)
                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                    drawable.draw(canvas)
                    loadedBitmap = bitmap
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ApodDetailActivity, "圖片載入失敗", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 點擊圖片產生星空卡並分享
        imageDetail.setOnClickListener {
            val bitmap = loadedBitmap
            if (bitmap == null) {
                Toast.makeText(this, "圖片還在載入中，請稍後再試", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            CoroutineScope(Dispatchers.Main).launch {
                val cardBitmap = withContext(Dispatchers.Default) {
                    createStarCard(bitmap, title, date)
                }
                showStarCardDialog(cardBitmap)
            }
        }

        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            finish()
        }
    }

    private fun createStarCard(original: Bitmap, title: String, date: String): Bitmap {
        val width = original.width
        val height = original.height + 250

        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)

        canvas.drawColor(Color.BLACK)
        canvas.drawBitmap(original, 0f, 0f, null)

        val paint = Paint()
        paint.color = Color.argb(180, 0, 0, 0)
        canvas.drawRect(0f, original.height.toFloat(), width.toFloat(), height.toFloat(), paint)

        // 標題字縮小成 36f，並限制寬度自動換行
        val titlePaint = Paint().apply {
            color = Color.WHITE
            textSize = 50f
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }

        // 自動換行處理
        val maxWidth = width - 60f
        var y = original.height + 60f
        var start = 0
        while (start < title.length) {
            val count = titlePaint.breakText(title, start, title.length, true, maxWidth, null)
            canvas.drawText(title, start, start + count, 30f, y, titlePaint)
            y += 50f
            start += count
        }

        val datePaint = Paint().apply {
            color = Color.LTGRAY
            textSize = 35f
            isAntiAlias = true
        }
        canvas.drawText("📅 $date", 30f, y + 10f, datePaint)

        val watermarkPaint = Paint().apply {
            color = Color.argb(150, 255, 255, 255)
            textSize = 30f
            isAntiAlias = true
        }
        canvas.drawText("NASA APOD・Nova", 30f, y + 55f, watermarkPaint)

        return result
    }

    private fun shareImage(bitmap: Bitmap) {
        try {
            // 儲存到暫存資料夾
            val cachePath = File(cacheDir, "images")
            cachePath.mkdirs()
            val file = File(cachePath, "star_card.png")
            FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }

            // 用 FileProvider 取得 URI
            val uri: Uri = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                file
            )

            // 呼叫系統分享
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "🌌 $title・$date\n\n透過 Nova 探索宇宙")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(shareIntent, "分享星空卡"))
        } catch (e: Exception) {
            Toast.makeText(this, "分享失敗：${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showStarCardDialog(cardBitmap: Bitmap) {
        val dialog = android.app.Dialog(this)
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.activity_star_card)
        dialog.window?.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.argb(180, 0, 0, 0)))
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.MATCH_PARENT
        )

        dialog.findViewById<ImageView>(R.id.imageStarCard).setImageBitmap(cardBitmap)

        dialog.findViewById<ImageButton>(R.id.buttonShare).setOnClickListener {
            shareImage(cardBitmap)
            dialog.dismiss()
        }

        dialog.show()
    }
}