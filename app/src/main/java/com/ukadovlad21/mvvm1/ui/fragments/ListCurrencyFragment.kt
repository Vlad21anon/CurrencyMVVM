package com.ukadovlad21.mvvm1.ui.fragments

import android.os.Bundle
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

    private lateinit var currencyAdapter: CurrencyAdapter

    private val viewModel by lazy {
        (activity as CurrencyActivity).currencyViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        currencyAdapter = CurrencyAdapter {
            viewModel.saveCurrency(it)
            Toast.makeText(requireContext(), "${it.name} Saved", Toast.LENGTH_SHORT).show()
        }
        setupRecyclerView()

        viewModel.getCurrency().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoadingBar()
                    response.data.let { responses ->
                        val actualString = "Actual at " + responses.date

                        tvActualAt.text = actualString

                        val listCurrencyNameAndPrice: List<CurrencyNameAndPrice> =
                            viewModel.responseToList(responses)

                        currencyAdapter.submitList(listCurrencyNameAndPrice)
                    }
                }
                is Resource.Error -> {
                    hideLoadingBar()
                    response.message.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is Resource.Loading -> showLoadingBar()

            }

        }

    }


    private fun hideLoadingBar() {
        loadingProgressBar.visibility = View.GONE
        rvListCur.visibility = View.VISIBLE
    }

    private fun showLoadingBar() {
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