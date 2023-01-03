package com.ukadovlad21.mvvm1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ukadovlad21.mvvm1.R
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice
import kotlinx.android.synthetic.main.currency_item.*
import kotlinx.android.synthetic.main.currency_item.view.*


class CurrencyAdapter: ListAdapter<CurrencyNameAndPrice, CurrencyItemHolder>(ItemComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyItemHolder =
        CurrencyItemHolder(parent)
    override fun onBindViewHolder(holder: CurrencyItemHolder, position: Int) {
        holder.bind(getItem(position))
        if (getItem(position).isSaved) {
            holder.itemView.ibSave.setImageResource(R.drawable.ic_baseline_favorite_36)
        } else {
            holder.itemView.ibSave.setImageResource(R.drawable.ic_baseline_favorite_border_36)
            holder.itemView.ibSave.setOnClickListener {
                holder.itemView.ibSave.isClickable = false
                getItem(position).isSaved = true
                holder.itemView.ibSave.setImageResource(R.drawable.ic_baseline_favorite_36)
                saveItemToDb?.let { it(getItem(position)) }
            }
        }


    }
    class ItemComparator:DiffUtil.ItemCallback<CurrencyNameAndPrice>(){
        override fun areItemsTheSame(oldItem: CurrencyNameAndPrice, newItem: CurrencyNameAndPrice): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: CurrencyNameAndPrice, newItem: CurrencyNameAndPrice): Boolean {
            return oldItem == newItem
        }
    }
    private var saveItemToDb: ((CurrencyNameAndPrice) -> Unit)? = null
    fun saveItemToDb(listener: (CurrencyNameAndPrice) -> Unit) {
        saveItemToDb = listener
    }
}

class CurrencyItemHolder(container:ViewGroup):ViewHolder(
    LayoutInflater.from(container.context).inflate(
        R.layout.currency_item, container, false)
) {
    fun bind(currency: CurrencyNameAndPrice) {
        itemView.apply {
            tvCurName.text = currency.name
            val price = if (currency.price.toString().length<8) currency.price.toString()
            else currency.price.toString().substring(0,8)
            tvCurPrice.text = price


        }

    }
}

