package com.example.swapprofit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishlistItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val item: String,
    val price: Double,
    val link: String,
    val dateAdded: Long = System.currentTimeMillis()
)
