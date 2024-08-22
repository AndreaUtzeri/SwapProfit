package com.example.swapprofit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WishlistDao {
    @Insert
    fun insert(item: WishlistItem)

    @Query("SELECT * FROM wishlist")
    fun getAll(): List<WishlistItem>
}
