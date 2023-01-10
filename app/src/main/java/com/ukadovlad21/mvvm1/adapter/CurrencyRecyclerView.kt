package com.ukadovlad21.mvvm1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ukadovlad21.mvvm1.R
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice
import kotlinx.android.synthetic.main.currency_item.view.*


class CurrencyAdapter(
    private val saveItemToDb: ((CurrencyNameAndPrice) -> Unit)
) : ListAdapter<CurrencyNameAndPrice, CurrencyItemHolder>(ItemComparator()) {
    class ItemComparator : DiffUtil.ItemCallback<CurrencyNameAndPrice>() {

        override fun areItemsTheSame(
            oldItem: CurrencyNameAndPrice,
            newItem: CurrencyNameAndPrice
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CurrencyNameAndPrice,
            newItem: CurrencyNameAndPrice
        ): Boolean {
            return oldItem == newItem
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyItemHolder =
        CurrencyItemHolder(saveItemToDb, parent)

    override fun onBindViewHolder(holder: CurrencyItemHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class CurrencyItemHolder(
    val saveItemToDb: ((CurrencyNameAndPrice) -> Unit),
    container: ViewGroup
) : ViewHolder(
    LayoutInflater.from(container.context).inflate(
        R.layout.currency_item, container, false
    )
) {
    fun bind(currency: CurrencyNameAndPrice) {
        itemView.apply {
            tvCurName.text = currency.name
            val price = if (currency.price.toString().length < 8) currency.price.toString()
            else currency.price.toString().substring(0, 8)
            tvCurPrice.text = price
        }
        if (currency.isSaved) {
            itemView.ibSave.setImageResource(R.drawable.ic_baseline_favorite_36)
            itemView.ibSave.isClickable = false
        } else {
            itemView.ibSave.setImageResource(R.drawable.ic_baseline_favorite_border_36)

            itemView.ibSave.setOnClickListener {
                itemView.ibSave.isClickable = false
                currency.isSaved = true
                itemView.ibSave.setImageResource(R.drawable.ic_baseline_favorite_36)
                saveItemToDb(currency)
            }
        }

    }
}

