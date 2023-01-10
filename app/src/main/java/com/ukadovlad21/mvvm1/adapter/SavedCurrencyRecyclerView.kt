package com.ukadovlad21.mvvm1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ukadovlad21.mvvm1.R
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice
import kotlinx.android.synthetic.main.favorite_currency_item.view.*
import kotlinx.android.synthetic.main.favorite_currency_item.view.tvCurName
import kotlinx.android.synthetic.main.favorite_currency_item.view.tvCurPrice


class SavedCurrencyAdapter :
    ListAdapter<CurrencyNameAndPrice, SavedCurrencyItemHolder>(ItemComparator()) {

    class ItemComparator : DiffUtil.ItemCallback<CurrencyNameAndPrice>() {
        override fun areItemsTheSame(
            oldItem: CurrencyNameAndPrice,
            newItem: CurrencyNameAndPrice
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CurrencyNameAndPrice,
            newItem: CurrencyNameAndPrice
        ): Boolean {
            return oldItem.name == newItem.name
        }
    }

    private var _setOnClickRefresh: ((CurrencyNameAndPrice) -> Unit)? = null
    fun setOnClickRefresh(listener: (CurrencyNameAndPrice) -> Unit) {
        _setOnClickRefresh = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedCurrencyItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_currency_item, parent, false)
        return SavedCurrencyItemHolder(view)
    }

    override fun onBindViewHolder(holder: SavedCurrencyItemHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.ibRefresh.setOnClickListener {
            _setOnClickRefresh?.let { it(getItem(position)) }
        }

    }


}

class SavedCurrencyItemHolder(container: View) : RecyclerView.ViewHolder(container) {
    fun bind(currency: CurrencyNameAndPrice) {
        itemView.apply {
            tvCurName.text = currency.name
            val price = if (currency.price.toString().length < 8) currency.price.toString()
            else currency.price.toString().substring(0, 8)
            tvCurPrice.text = price

            val actualStr = "Actual at ${currency.actualAt}"
            tvSavedActualAt.text = actualStr

        }

    }
}


