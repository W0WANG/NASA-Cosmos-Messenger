package com.example.nasacosmosmessenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nasacosmosmessenger.R


class MessageAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layoutNova: LinearLayout = view.findViewById(R.id.layoutNova)
        val textNovaMessage: TextView = view.findViewById(R.id.textNovaMessage)

        // 卡片元件
        val apodCard: View = view.findViewById(R.id.apodCard)
        val imageApodCard: ImageView = apodCard.findViewById(R.id.imageApodCard)
        val textApodTitle: TextView = apodCard.findViewById(R.id.textApodTitle)
        val textApodDesc: TextView = apodCard.findViewById(R.id.textApodDesc)

        val layoutUser: LinearLayout = view.findViewById(R.id.layoutUser)
        val textUserMessage: TextView = view.findViewById(R.id.textUserMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        if (message.isUser) {
            // 是使用者發的：顯示右邊，藏起左邊
            holder.layoutUser.visibility = View.VISIBLE
            holder.layoutNova.visibility = View.GONE
            holder.textUserMessage.text = message.content
        } else {
            // 是 Nova 發的：顯示左邊，藏起右邊
            holder.layoutNova.visibility = View.VISIBLE
            holder.layoutUser.visibility = View.GONE
            holder.textNovaMessage.text = message.content

            // 處理 NASA 天文圖
            if (message.apodData != null) {
                holder.apodCard.visibility = View.VISIBLE
                holder.imageApodCard.load(message.apodData.url) { crossfade(true) }
                holder.textApodTitle.text = message.apodData.title
                holder.textApodDesc.text = message.apodData.explanation
            } else {
                holder.apodCard.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = messages.size
}