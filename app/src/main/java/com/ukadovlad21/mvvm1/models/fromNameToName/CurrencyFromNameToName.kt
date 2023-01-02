package com.ukadovlad21.mvvm1.models.fromNameToName

data class CurrencyFromNameToName(
    val date: String,
    val historical: Boolean,
    val info: Info,
    val motd: MotdX,
    val query: Query,
    val result: Double,
    val success: Boolean
)