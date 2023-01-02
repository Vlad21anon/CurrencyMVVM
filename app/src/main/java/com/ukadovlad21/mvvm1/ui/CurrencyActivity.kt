package com.ukadovlad21.mvvm1.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ukadovlad21.mvvm1.R
import com.ukadovlad21.mvvm1.db.CurrencyDatabase
import com.ukadovlad21.mvvm1.repository.CurrencyRepository
import kotlinx.android.synthetic.main.activity_currency.*


class CurrencyActivity : AppCompatActivity() {

    private var currencyRepository: CurrencyRepository? = null
    val currencyViewModel: CurrencyViewModel by viewModels {
        CurrencyViewModelProviderFactory(application,currencyRepository!!)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currencyRepository = CurrencyRepository(CurrencyDatabase(this))

        setContentView(R.layout.activity_currency)
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())

    }



}