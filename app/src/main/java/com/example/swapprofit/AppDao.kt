package com.example.swapprofit.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


        //CLASSE COMPLETAMENTE DA RIFARE
@Dao
interface AppDao {

    @Insert
    fun insertWishlistItem(item: WishlistItem)

    @Query("SELECT SUM(price) FROM wishlist_items")
    fun getTotalWishlistValue(): Double

    @Query("SELECT * FROM wishlist_items")
    fun getAllWishlistItems(): List<WishlistItem>
}
