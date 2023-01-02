package com.ukadovlad21.mvvm1.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ukadovlad21.mvvm1.models.CurrencyNameAndPrice

@Database(
    entities = [CurrencyNameAndPrice::class],
    version = 3
)

//@TypeConverters(Converters::class)
abstract class CurrencyDatabase:RoomDatabase() {

    abstract fun getCurrencyDao(): CurrencyDao

    companion object {
        @Volatile
        private var instance:CurrencyDatabase?=null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context)= Room.databaseBuilder(
            context.applicationContext,CurrencyDatabase::class.java,"saved_currency_db.db"
        ).build()

    }

}