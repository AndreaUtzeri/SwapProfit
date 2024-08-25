package com.example.swapprofit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PurchaseDao {
    @Insert
    fun insert(purchase: Purchase)

    @Query("SELECT * FROM purchases")
    fun getAll(): List<Purchase>

    @Update
    suspend fun update(purchase: Purchase)

    @Delete
    suspend fun delete(purchase: Purchase)
}
