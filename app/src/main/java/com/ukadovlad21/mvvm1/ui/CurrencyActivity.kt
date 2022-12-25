package com.ukadovlad21.mvvm1.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ukadovlad21.mvvm1.R
import kotlinx.android.synthetic.main.activity_currency.*

class CurrencyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency)

        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}