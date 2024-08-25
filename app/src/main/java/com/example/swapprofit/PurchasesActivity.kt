package com.example.swapprofit

import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

        setupRecyclerView()

        binding.btnSavePurchase.setOnClickListener {
            val item = binding.etPurchaseItem.text.toString()
            val price = binding.etPurchasePrice.text.toString().toDoubleOrNull()

            if (item.isNotBlank() && price != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val purchase = Purchase(item = item, price = price)
                    database.purchaseDao().insert(purchase)
                    setupRecyclerView()
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

    private fun setupRecyclerView() {
        CoroutineScope(Dispatchers.IO).launch {
            val purchasesList = database.purchaseDao().getAll()
            withContext(Dispatchers.Main) {
                binding.rvPurchasesList.layoutManager = LinearLayoutManager(this@PurchasesActivity)
                binding.rvPurchasesList.adapter = PurchaseItemAdapter(purchasesList, ::editPurchase, ::deletePurchase)
            }
        }
    }
    private fun editPurchase(purchase: Purchase) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_purchase, null)
        builder.setView(dialogView)
        val dialog = builder.create()

        val etItemName = dialogView.findViewById<EditText>(R.id.etItemName)
        val etItemPrice = dialogView.findViewById<EditText>(R.id.etItemPrice)

        etItemName.setText(purchase.item)
        etItemPrice.setText(purchase.price.toString())

        dialogView.findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            val newItem = etItemName.text.toString()
            val newPrice = etItemPrice.text.toString().toDoubleOrNull()

            if (newItem.isNotBlank() && newPrice != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    database.purchaseDao().update(Purchase(purchase.id, newItem, newPrice))
                    withContext(Dispatchers.Main) {
                        setupRecyclerView()
                        dialog.dismiss()
                    }
                }
            }
        }

        dialog.show()
    }
    private fun deletePurchase(purchase: Purchase) {
        CoroutineScope(Dispatchers.IO).launch {
            database.purchaseDao().delete(purchase)
            withContext(Dispatchers.Main) {
                setupRecyclerView()
            }
        }
    }
}
