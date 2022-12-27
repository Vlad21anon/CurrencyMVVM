package com.ukadovlad21.mvvm1.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ukadovlad21.mvvm1.R
import com.ukadovlad21.mvvm1.db.CurrencyDatabase
import com.ukadovlad21.mvvm1.repository.CurrencyRepository
import kotlinx.android.synthetic.main.activity_currency.*


class CurrencyActivity : AppCompatActivity() {

    lateinit var viewModelProviderFactory: CurrencyViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency)

        val currencyRepository = CurrencyRepository(CurrencyDatabase(this))
        viewModelProviderFactory = CurrencyViewModelProviderFactory(application,currencyRepository)


        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())

    }



}