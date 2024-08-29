package com.example.swapprofit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sales")
data class Sale(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val item: String,
    val price: Double,
    val dateAdded: Long = System.currentTimeMillis()
)
