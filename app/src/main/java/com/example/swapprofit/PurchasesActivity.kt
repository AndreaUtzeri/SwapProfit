package com.example.swapprofit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.swapprofit.databinding.ActivityPurchasesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PurchasesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPurchasesBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchasesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        binding.btnSavePurchase.setOnClickListener {
            val item = binding.etPurchaseItem.text.toString()
            val price = binding.etPurchasePrice.text.toString().toDoubleOrNull()

            if (item.isNotBlank() && price != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val purchase = Purchase(item = item, price = price)
                    database.purchaseDao().insert(purchase)

                    // Log dei dati salvati
                    logSavedData()
                }

                Toast.makeText(this, "Purchase saved: $item for €$price", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter valid item and price", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logSavedData() {
        CoroutineScope(Dispatchers.IO).launch {
            val purchases = database.purchaseDao().getAll()
            Log.d("PurchasesActivity", "Saved Purchases: ${purchases.map { it.price }}")
        }
    }
}
