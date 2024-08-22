package com.example.swapprofit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SaleDao {
    @Insert
    fun insert(sale: Sale)

    @Query("SELECT * FROM sales")
    fun getAll(): List<Sale>
}
