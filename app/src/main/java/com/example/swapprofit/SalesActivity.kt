package com.example.swapprofit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swapprofit.databinding.ActivitySalesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SalesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySalesBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        setupRecyclerView() //Per il funzionamento della visualizzazione della lista

        binding.btnSaveSale.setOnClickListener {
            val item = binding.etSaleItem.text.toString()
            val price = binding.etSalePrice.text.toString().toDoubleOrNull()

            if (item.isNotBlank() && price != null) {
                // Salva la vendita nel database
                CoroutineScope(Dispatchers.IO).launch {
                    val sale = Sale(item = item, price = price)
                    database.saleDao().insert(sale)

                    // Log dei dati salvati
                    logSavedData()
                }

                Toast.makeText(this, "Sale saved: $item for €$price", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter valid item and price", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logSavedData() {
        CoroutineScope(Dispatchers.IO).launch {
            val sales = database.saleDao().getAll()
            Log.d("SalesActivity", "Saved Sales: ${sales.map { it.price }}")
        }
    }

    private fun setupRecyclerView() {
        CoroutineScope(Dispatchers.IO).launch {
            val salesList = database.saleDao().getAll()
            withContext(Dispatchers.Main) {
                binding.rvSalesList.layoutManager = LinearLayoutManager(this@SalesActivity)
                binding.rvSalesList.adapter = SaleItemAdapter(salesList)
            }
        }
    }
}
