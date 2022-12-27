package com.ukadovlad21.mvvm1.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukadovlad21.mvvm1.R
import com.ukadovlad21.mvvm1.adapter.CurrencyAdapter
import com.ukadovlad21.mvvm1.adapter.SavedCurrencyAdapter
import com.ukadovlad21.mvvm1.ui.CurrencyActivity
import com.ukadovlad21.mvvm1.ui.CurrencyViewModel
import kotlinx.android.synthetic.main.fragment_list_currency.*

class SavedCurrencyFragment : Fragment(R.layout.fragment_saved_currency) {

    lateinit var viewModel:CurrencyViewModel
    lateinit var savedCurrencyAdapter: SavedCurrencyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel = (activity as CurrencyActivity).viewModel

        setupRecyclerView()

        viewModel.getSavedCurrencies().observe(viewLifecycleOwner, Observer {
//            savedCurrencyAdapter.differ.submitList(it)
        })
    }

    private fun setupRecyclerView() {
        savedCurrencyAdapter = SavedCurrencyAdapter()
        rvListCur.apply {
            adapter = savedCurrencyAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


}