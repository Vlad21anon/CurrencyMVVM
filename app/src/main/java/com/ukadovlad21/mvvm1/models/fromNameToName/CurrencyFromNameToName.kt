package com.ukadovlad21.mvvm1.models.fromNameToName

import com.google.gson.annotations.SerializedName
import com.ukadovlad21.mvvm1.models.Motd

data class CurrencyFromNameToName(
    val date: String,
    val historical: Boolean,
    val info: Info,
    @SerializedName("motd")
    val authorMessage: Motd,
    val query: Query,
    val result: Double,
    val success: Boolean
)