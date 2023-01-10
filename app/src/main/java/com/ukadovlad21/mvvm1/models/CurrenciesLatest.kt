package com.ukadovlad21.mvvm1.models

import com.google.gson.annotations.SerializedName

data class CurrenciesLatest(
    val base: String,
    val date: String,
    @SerializedName("motd")
    val authorMessage: Motd,
    val rates: Map<String, Double>,
    val success: Boolean
)

