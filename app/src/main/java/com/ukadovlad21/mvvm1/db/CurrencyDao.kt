package com.ukadovlad21.mvvm1.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(currencyNameAndPrice: CurrencyNameAndPrice): Long

    @Query("SELECT * FROM currencies")
    fun getAllSavedCurrencies(): LiveData<List<CurrencyNameAndPrice>>

    @Delete
    suspend fun deleteArticle(currencyNameAndPrice: CurrencyNameAndPrice)

}
