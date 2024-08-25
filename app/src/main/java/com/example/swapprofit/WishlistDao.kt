package com.example.swapprofit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WishlistDao {
    @Insert
    fun insert(item: WishlistItem)

    @Query("SELECT * FROM wishlist")
    fun getAll(): List<WishlistItem>

    @Update
    suspend fun update(wishlistItem: WishlistItem)

    @Delete
    suspend fun delete(wishlistItem: WishlistItem)
}
