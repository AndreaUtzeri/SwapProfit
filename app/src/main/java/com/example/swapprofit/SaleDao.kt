package com.example.swapprofit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface SaleDao {
    @Insert
    fun insert(sale: Sale)

    @Query("SELECT * FROM sales")
    fun getAll(): List<Sale>

    @Update
    suspend fun update(sale: Sale)

    @Delete
    suspend fun delete(sale: Sale): Int  // per dare un tipo di ritorno

    @Query("SELECT * FROM purchases ORDER BY dateAdded DESC")
    fun getAllByDate(): List<Purchase>

    @Query("SELECT * FROM purchases ORDER BY price ASC")
    fun getAllByPriceAscending(): List<Purchase>

    @Query("SELECT * FROM purchases ORDER BY price DESC")
    fun getAllByPriceDescending(): List<Purchase>
}
