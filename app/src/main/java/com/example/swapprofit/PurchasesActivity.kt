package com.example.swapprofit

import android.media.MediaPlayer
import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchasesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        mediaPlayer = MediaPlayer.create(this, R.raw.pay)

        setupSpinner()

        setupRecyclerView()

        binding.btnSavePurchase.setOnClickListener {
            val item = binding.etPurchaseItem.text.toString()
            val price = binding.etPurchasePrice.text.toString().toDoubleOrNull()

            if (item.isNotBlank() && price != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val purchase = Purchase(item = item, price = price)
                    database.purchaseDao().insert(purchase)

                    mediaPlayer.start()

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

    private fun setupSpinner() {
        val spinner: Spinner = binding.spinnerSortOptions
        val sortOptions = arrayOf("Sort by Date (Newest)", "Sort by Date (Oldest)", "Sort by Price (Low to High)", "Sort by Price (High to Low)")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Per impostare il listener in modo esplicito con AdapterView.OnItemSelectedListener
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                setupRecyclerView(sortOption = position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Non fare nulla se nessuna opzione è selezionata
            }
        }
    }


    private fun setupRecyclerView(sortOption: Int = 0) {
        CoroutineScope(Dispatchers.IO).launch {
            val purchasesList = when (sortOption) {
                1 -> database.purchaseDao().getAll().sortedBy { it.id }  // Sort by Date (Oldest)
                2 -> database.purchaseDao().getAll().sortedBy { it.price }  // Sort by Price (Low to High)
                3 -> database.purchaseDao().getAll().sortedByDescending { it.price }  // Sort by Price (High to Low)
                else -> database.purchaseDao().getAll().sortedByDescending { it.id }  // Sort by Date (Newest)
            }
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
