package com.ukadovlad21.mvvm1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ukadovlad21.mvvm1.R
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice
import kotlinx.android.synthetic.main.currency_item.*
import kotlinx.android.synthetic.main.currency_item.view.*

class CurrencyAdapter: RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {
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
        return CurrencyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.currency_item,parent,false))
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = differ.currentList[position]
        holder.itemView.apply {
            tvCurName.text = currency.name
            tvCurPrice.text = currency.price.toString()

            ibSave.setImageResource(R.drawable.ic_baseline_favorite_border_36)
            if (currency.isSaved) {
                ibSave.setImageResource(R.drawable.ic_baseline_favorite_36)
            }
            ibSave.setOnClickListener {
                currency.isSaved = true
                ibSave.setImageResource(R.drawable.ic_baseline_favorite_36)
                saveItemToDb?.let { it(currency) }
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var saveItemToDb: ((CurrencyNameAndPrice) -> Unit)? = null
    fun saveItemToDb(listener: (CurrencyNameAndPrice) -> Unit) {
        saveItemToDb = listener
    }

}

