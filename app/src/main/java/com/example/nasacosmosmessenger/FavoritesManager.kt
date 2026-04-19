package com.example.nasacosmosmessenger

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object FavoritesManager {
    private const val PREF_NAME = "favorites"
    private const val KEY_LIST = "favorites_list"
    private val gson = Gson()

    fun getAll(context: Context): MutableList<ApodResponse> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_LIST, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<ApodResponse>>() {}.type
        return gson.fromJson(json, type)
    }

    fun add(context: Context, apod: ApodResponse) {
        val list = getAll(context)
        if (list.none { it.date == apod.date }) {  // 避免重複
            list.add(apod)
            save(context, list)
        }
    }

    fun remove(context: Context, apod: ApodResponse) {
        val list = getAll(context)
        list.removeAll { it.date == apod.date }
        save(context, list)
    }

    fun isFavorite(context: Context, apod: ApodResponse): Boolean {
        return getAll(context).any { it.date == apod.date }
    }

    private fun save(context: Context, list: MutableList<ApodResponse>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LIST, gson.toJson(list)).apply()
    }
}