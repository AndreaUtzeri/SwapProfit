package com.example.swapprofit.data

import androidx.room.Entity
import androidx.room.PrimaryKey
        //classe completamente da rifare
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemName: String,
    val amount: Double
)
