package com.example.swapprofit.data

import androidx.room.Entity
import androidx.room.PrimaryKey
        //CLASSE COMPLETAMENTE DA RIFARE
@Entity(tableName = "wishlist_items")
data class WishlistItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemName: String,
    val price: Double,
    val link: String
)
