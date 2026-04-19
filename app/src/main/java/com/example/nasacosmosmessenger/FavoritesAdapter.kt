package com.example.nasacosmosmessenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load

class FavoritesAdapter(
    private val favorites: MutableList<ApodResponse>,
    private val onRemove: (ApodResponse) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imageFavorite)
        val title: TextView = view.findViewById(R.id.textFavoriteTitle)
        val date: TextView = view.findViewById(R.id.textFavoriteDate)
        val desc: TextView = view.findViewById(R.id.textFavoriteDesc)
        val buttonUnfavorite: ImageButton = view.findViewById(R.id.buttonUnfavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val apod = favorites[position]

        holder.image.load(apod.url) { crossfade(true) }
        holder.title.text = apod.title
        holder.date.text = apod.date
        holder.desc.text = apod.explanation

        // 點星星取消收藏
        holder.buttonUnfavorite.setOnClickListener {
            FavoritesManager.remove(holder.itemView.context, apod)
            favorites.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, favorites.size)
            Toast.makeText(holder.itemView.context, "已取消收藏", Toast.LENGTH_SHORT).show()
            onRemove(apod)
        }
    }

    override fun getItemCount(): Int = favorites.size
}