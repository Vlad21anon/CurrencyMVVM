package com.ukadovlad21.mvvm1.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukadovlad21.mvvm1.R
import com.ukadovlad21.mvvm1.adapter.CurrencyAdapter
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice
import com.ukadovlad21.mvvm1.ui.CurrencyActivity
import com.ukadovlad21.mvvm1.utils.Resource
import kotlinx.android.synthetic.main.fragment_list_currency.*

class ListCurrencyFragment : Fragment(R.layout.fragment_list_currency) {

    val currencyAdapter = CurrencyAdapter()

    val viewModel by lazy {
        (activity as CurrencyActivity).currencyViewModel
    }

    @Suppress("NAME_SHADOWING")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        currencyAdapter.saveItemToDb {
            viewModel.saveCurrency(it)
            Toast.makeText(requireContext(),"${it.name} Saved",Toast.LENGTH_SHORT).show()
        }

        viewModel.currency.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()
                    response.data?.let { response ->
                        val actualString = "Actual at "+ response.date
                        tvActualAt.text = actualString
                        val listCurrencyNameAndPrice: List<CurrencyNameAndPrice> = responseToList(response.rates,response.date)
                        currencyAdapter.submitList(listCurrencyNameAndPrice)
                    }
                }
                is Resource.Error -> {
                    hideLoading()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is Resource.Loading -> { showLoading() }

            }

        }

    }



    private fun responseToList(rates: Map<String, Double>, data: String): MutableList<CurrencyNameAndPrice> {
        var list: MutableList<CurrencyNameAndPrice> = mutableListOf()
        rates.forEach { map ->
            val item = CurrencyNameAndPrice(null,
                map.key,
                map.value,
                data,
                isSavedToDb(map.key)
            )
            list.add(item)
        }
        return list
    }
    private fun isSavedToDb(name:String):Boolean{
        viewModel.getSavedCurrencies().observe(viewLifecycleOwner) {



        }
        return false //Должен вернуть true/false
    }


    private fun hideLoading() {
        loadingProgressBar.visibility = View.GONE
        rvListCur.visibility = View.VISIBLE
    }
    private fun showLoading() {
        loadingProgressBar.visibility = View.VISIBLE
        rvListCur.visibility = View.GONE
    }



    private fun setupRecyclerView() {
        rvListCur.apply {
            adapter = currencyAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}