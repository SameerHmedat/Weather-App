package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.dataClass.CardDay
import com.example.weatherapp.R
import kotlinx.android.synthetic.main.card_day_item.view.*

class CardDayAdapter (private var cardDayList: List<CardDay>) :
RecyclerView.Adapter<CardDayAdapter.CardDayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDayViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.card_day_item, parent, false
        )

        return CardDayViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardDayViewHolder, position: Int) {
        val currentItem: CardDay = cardDayList[position]
        holder.bind(currentItem)

    }

    override fun getItemCount(): Int {
        return cardDayList.size
    }

    class CardDayViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(cardDay: CardDay) {
            itemView.cardSunRise.text = cardDay.sunRise
            itemView.cardDate.text = cardDay.date
            itemView.cardDayDegree.text = cardDay.degree
        }


    }
}
