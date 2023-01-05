package com.ukadovlad21.mvvm1.models

data class CurrenciesLatest(
    val base: String,
    val date: String,
    val motd: Motd,
    val rates: Map<String, Double>,
    val success: Boolean
)

