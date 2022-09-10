package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.dataClass.Card
import com.example.weatherapp.R
import kotlinx.android.synthetic.main.card_item.view.*

class CardAdapter (private var cardList: List<Card>) :
RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.card_item, parent, false
        )

        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem: Card = cardList[position]
        holder.bind(currentItem)

    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    class CardViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(card: Card) {
            itemView.cardSunRise.text = card.sunRise
            itemView.cardDate.text = card.date
            itemView.cardDegree.text = card.degree
        }


    }
}
