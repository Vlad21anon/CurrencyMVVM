package com.ukadovlad21.mvvm1.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.ukadovlad21.mvvm1.R
import com.ukadovlad21.mvvm1.ui.CurrencyActivity
import com.ukadovlad21.mvvm1.utils.Resource
import kotlinx.android.synthetic.main.fragment_exchange_currency.*

class ExchangeCurrencyFragment : Fragment(R.layout.fragment_exchange_currency) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = (activity as CurrencyActivity).currencyViewModel


        btnConvert.setOnClickListener {
            val toName = spTo.selectedItem.toString()
            val fromName = spFrom.selectedItem.toString()
            val amount = tietFrom.text.toString().toInt()

            viewModel.convertByNames(fromName, toName, amount)
            viewModel.convertCurrency.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        hideLoading()
                        tvResult.text = response.data?.result.toString()
                        tvDate.text = response.data?.date.toString()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        response.message?.let { message ->
                            Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    is Resource.Loading -> {
                        showLoading()
                    }
                }
            }
        }

    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.INVISIBLE
    }


}

