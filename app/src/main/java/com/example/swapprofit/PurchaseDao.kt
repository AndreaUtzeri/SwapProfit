package com.example.swapprofit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PurchaseDao {
    @Insert
    fun insert(purchase: Purchase)

    @Query("SELECT * FROM purchases")
    fun getAll(): List<Purchase>
}
