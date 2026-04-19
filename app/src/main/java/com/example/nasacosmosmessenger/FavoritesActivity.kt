package com.example.nasacosmosmessenger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        recyclerView = findViewById(R.id.recyclerFavorites)
        val favorites = FavoritesManager.getAll(this)
        adapter = FavoritesAdapter(favorites) { }
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        // 底部導覽列
        val bottomNav = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_favorites  // 預設選中收藏
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_nova -> {
                    finish()  // 回到 MainActivity
                    true
                }
                R.id.nav_favorites -> true
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 每次回到頁面時重新載入收藏清單
        val favorites = FavoritesManager.getAll(this)
        adapter = FavoritesAdapter(favorites) { }
        recyclerView.adapter = adapter
    }
}