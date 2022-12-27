package com.ukadovlad21.mvvm1.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(
    tableName = "currencies"
)
data class CurrencyNameAndPrice(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    val name:String,
    val price:Double,
    var isSaved:Boolean
):Serializable