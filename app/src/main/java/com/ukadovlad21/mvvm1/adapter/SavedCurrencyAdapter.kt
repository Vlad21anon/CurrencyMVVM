package com.ukadovlad21.mvvm1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ukadovlad21.mvvm1.R
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice
import kotlinx.android.synthetic.main.currency_item.view.*
import kotlinx.android.synthetic.main.favorite_currency_item.*
import kotlinx.android.synthetic.main.favorite_currency_item.view.*
import kotlinx.android.synthetic.main.favorite_currency_item.view.tvCurName
import kotlinx.android.synthetic.main.favorite_currency_item.view.tvCurPrice


class SavedCurrencyAdapter: ListAdapter<CurrencyNameAndPrice, SavedCurrencyItemHolder>(ItemComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedCurrencyItemHolder =
        SavedCurrencyItemHolder(parent)
    override fun onBindViewHolder(holder: SavedCurrencyItemHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.ibRefresh.setOnClickListener {
            refreshClickListener?.let { it(getItem(position)) }
        }

    }
    class ItemComparator:DiffUtil.ItemCallback<CurrencyNameAndPrice>(){
        override fun areItemsTheSame(oldItem: CurrencyNameAndPrice, newItem: CurrencyNameAndPrice): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: CurrencyNameAndPrice, newItem: CurrencyNameAndPrice): Boolean {
            return oldItem.name == newItem.name
        }
    }

    var refreshClickListener : ((CurrencyNameAndPrice) -> Unit)? = null
    fun setOnClickRefresh(listener: (CurrencyNameAndPrice) -> Unit){
        refreshClickListener = listener
    }
}

class SavedCurrencyItemHolder(container:ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(container.context).inflate(
        R.layout.favorite_currency_item, container, false)
) {
    fun bind(currency: CurrencyNameAndPrice) {
        itemView.apply {
            tvCurName.text = currency.name
            val price = if (currency.price.toString().length<8) currency.price.toString()
            else currency.price.toString().substring(0,8)
            tvCurPrice.text = price

            val actualStr = "Actual at ${currency.actualAt}"
            tvSavedActualAt.text = actualStr

        }

    }
}


