package com.ukadovlad21.mvvm1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ukadovlad21.mvvm1.R
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice
import kotlinx.android.synthetic.main.favorite_currency_item.view.*

class SavedCurrencyAdapter: RecyclerView.Adapter<SavedCurrencyAdapter.CurrencyViewHolder>() {
    inner class CurrencyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)


    private val differCallback = object : DiffUtil.ItemCallback<CurrencyNameAndPrice>() {
        override fun areItemsTheSame(oldItem: CurrencyNameAndPrice, newItem: CurrencyNameAndPrice): Boolean {
            return oldItem.name == newItem.name
        }
        override fun areContentsTheSame(oldItem: CurrencyNameAndPrice, newItem: CurrencyNameAndPrice): Boolean {
            return oldItem.name == newItem.name
        }


    }
    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.favorite_currency_item,parent,false))
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = differ.currentList[position]
        holder.itemView.apply {
            tvCurName.text = currency.name
            tvCurPrice.text = currency.price.toString()
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}

