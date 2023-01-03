package com.ukadovlad21.mvvm1.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ukadovlad21.mvvm1.R
import com.ukadovlad21.mvvm1.adapter.SavedCurrencyAdapter
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice
import com.ukadovlad21.mvvm1.ui.CurrencyActivity
import com.ukadovlad21.mvvm1.utils.Resource
import kotlinx.android.synthetic.main.fragment_saved_currency.*

class SavedCurrencyFragment : Fragment(R.layout.fragment_saved_currency) {
    val savedCurrencyAdapter = SavedCurrencyAdapter()

    val viewModel by lazy {
        (activity as CurrencyActivity).currencyViewModel
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        viewModel.getSavedCurrencies().observe(viewLifecycleOwner) {
            savedCurrencyAdapter.submitList(it)
        }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val currency = savedCurrencyAdapter.currentList[position]
                viewModel.deleteSavedCurrency(currency)
                Snackbar.make(view, "Successfully deleted \"${currency.name}\"", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveCurrency(currency)
                    }
                    show()
                }
            }

        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedListCur)
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