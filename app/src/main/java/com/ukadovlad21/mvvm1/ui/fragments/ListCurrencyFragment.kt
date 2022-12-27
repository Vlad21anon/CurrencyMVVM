package com.ukadovlad21.mvvm1.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukadovlad21.mvvm1.R
import com.ukadovlad21.mvvm1.adapter.CurrencyAdapter
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice
import com.ukadovlad21.mvvm1.ui.CurrencyActivity
import com.ukadovlad21.mvvm1.ui.CurrencyViewModel
import com.ukadovlad21.mvvm1.utils.Resource
import kotlinx.android.synthetic.main.fragment_list_currency.*

class ListCurrencyFragment : Fragment(R.layout.fragment_list_currency) {

    private lateinit var currencyAdapter: CurrencyAdapter

    private var viewModel:CurrencyViewModel?= null

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        currencyAdapter.saveItemToDb {
            Toast.makeText(requireContext(),"Saved",Toast.LENGTH_SHORT).show()
            viewModel?.saveCurrency(it)
        }

            Handler().postDelayed({

            val viewModelProviderFactory = (activity as CurrencyActivity).viewModelProviderFactory
            viewModel = ViewModelProvider(this,viewModelProviderFactory).get(CurrencyViewModel::class.java)

            viewModel?.currency?.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()
                    response.data?.let { response ->
                        tvActualAt.text = "Actual at "+ response.date
                        val listCurrencyNameAndPrice: List<CurrencyNameAndPrice> = ratesToList(response.rates)
                        currencyAdapter.differ.submitList(listCurrencyNameAndPrice)
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
            },0L)
    }



    private fun ratesToList(rates: Map<String, Double>): MutableList<CurrencyNameAndPrice> {
        var list: MutableList<CurrencyNameAndPrice> = mutableListOf()
        rates.forEach { map ->
            val item = CurrencyNameAndPrice(null,map.key,map.value,isSavedToDb(map.key))
            list.add(item)
        }
        return list
    }
    private fun isSavedToDb(name:String):Boolean {
        val savedCurList = viewModel?.getSavedCurrencies()
        var isSaved = false
        savedCurList?.observe(viewLifecycleOwner) {
            it.forEach {
                if (it.name == name) {
                    isSaved = true
                }
            }

        }
        Log.d("TAG", "$name $isSaved")
        return isSaved
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
        currencyAdapter = CurrencyAdapter()
        rvListCur.apply {
            adapter = currencyAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}