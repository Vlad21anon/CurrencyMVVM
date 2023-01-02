package com.ukadovlad21.mvvm1.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukadovlad21.mvvm1.R
import com.ukadovlad21.mvvm1.adapter.SavedCurrencyAdapter
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice
import com.ukadovlad21.mvvm1.ui.CurrencyActivity
import com.ukadovlad21.mvvm1.utils.Resource
import kotlinx.android.synthetic.main.favorite_currency_item.*
import kotlinx.android.synthetic.main.fragment_saved_currency.*

class SavedCurrencyFragment : Fragment(R.layout.fragment_saved_currency) {
    val savedCurrencyAdapter = SavedCurrencyAdapter()

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = (activity as CurrencyActivity).currencyViewModel
        setupRecyclerView()
        viewModel.getSavedCurrencies().observe(viewLifecycleOwner) {
            savedCurrencyAdapter.submitList(it)
        }

        savedCurrencyAdapter.setOnClickRefresh { curNameAndPrice ->
            viewModel.getByNames("USD",curNameAndPrice.name)
            viewModel.refreshCurrency.observe(viewLifecycleOwner) {response ->
                when(response) {
                    is Resource.Success -> {
                        hideLoading()
                        if (response.data?.query?.to == curNameAndPrice.name) {
                            val newCurrency = CurrencyNameAndPrice(
                                curNameAndPrice.id,
                                curNameAndPrice.name, response.data!!.info.rate,
                                response.data.date, curNameAndPrice.isSaved
                            )
                            viewModel.updateCurrency(newCurrency)

                        }
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

    private fun showLoading() { pbSavedCurItem.visibility = View.VISIBLE }
    private fun hideLoading() { pbSavedCurItem.visibility = View.INVISIBLE }


    private fun setupRecyclerView() {
        rvSavedListCur.apply {
            adapter = savedCurrencyAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


}