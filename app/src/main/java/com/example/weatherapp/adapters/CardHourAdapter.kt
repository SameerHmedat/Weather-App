package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.dataClass.CardHour
import kotlinx.android.synthetic.main.card_hour_item.view.*

class CardHourAdapter (private var cardHourList: List<CardHour>) :
    RecyclerView.Adapter<CardHourAdapter.CardHourViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHourViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.card_hour_item, parent, false
        )

        return CardHourViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardHourViewHolder, position: Int) {
        val currentItem: CardHour = cardHourList[position]
        holder.bind(currentItem)

    }

    override fun getItemCount(): Int {
        return cardHourList.size
    }

    class CardHourViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(cardHour: CardHour) {
            itemView.cardHourHour.text=cardHour.cardHourHour
            itemView.cardDegreeHour.text=cardHour.cardDegreeHour
            itemView.cardHourDesc.text=cardHour.cardHourDesc
            Glide.with(itemView).load(cardHour.linkImage).into(itemView.imgCardHour)
        }


    }
}
