package com.example.swapprofit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AppDao {
    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Insert
    suspend fun insertWishlistItem(item: WishlistItem)

    @Query("SELECT SUM(price) FROM transactions WHERE type = 'sale'")
    suspend fun getTotalSales(): Double

    @Query("SELECT SUM(price) FROM transactions WHERE type = 'purchase'")
    suspend fun getTotalPurchases(): Double

    @Query("SELECT SUM(price) FROM wishlist_items")
    suspend fun getTotalWishlistValue(): Double
}
