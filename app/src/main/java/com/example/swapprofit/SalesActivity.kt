package com.example.swapprofit

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        setupSpinner()

        setupRecyclerView() //Per il funzionamento della visualizzazione della lista

        binding.btnSaveSale.setOnClickListener {
            val item = binding.etSaleItem.text.toString()
            val price = binding.etSalePrice.text.toString().toDoubleOrNull()

            if (item.isNotBlank() && price != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val sale = Sale(item = item, price = price)
                    database.saleDao().insert(Sale(0, item, price))
                    withContext(Dispatchers.Main) {
                        setupRecyclerView() // Mettendolo aggiorna appena messo un nuovo elemento.
                    }
                }
                Toast.makeText(this, "Sale saved: $item for €$price", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter valid item and price", Toast.LENGTH_SHORT).show()
            }
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
            val salesList = when (sortOption) {
                1 -> database.saleDao().getAll().sortedBy { it.id }  // Sort by Date (Oldest)
                2 -> database.saleDao().getAll().sortedBy { it.price }  // Sort by Price (Low to High)
                3 -> database.saleDao().getAll().sortedByDescending { it.price }  // Sort by Price (High to Low)
                else -> database.saleDao().getAll().sortedByDescending { it.id }  // Sort by Date (Newest)
            }
            withContext(Dispatchers.Main) {
                binding.rvSalesList.layoutManager = LinearLayoutManager(this@SalesActivity)
                binding.rvSalesList.adapter = SaleItemAdapter(salesList, ::editSale, ::deleteSale)
            }
        }
    }

    private fun editSale(sale: Sale) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_sale, null)
        builder.setView(dialogView)
        val dialog = builder.create()

        val etItemName = dialogView.findViewById<EditText>(R.id.etItemName)
        val etItemPrice = dialogView.findViewById<EditText>(R.id.etItemPrice)

        etItemName.setText(sale.item)
        etItemPrice.setText(sale.price.toString())

        dialogView.findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            val newItem = etItemName.text.toString()
            val newPrice = etItemPrice.text.toString().toDoubleOrNull()

            if (newItem.isNotBlank() && newPrice != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    database.saleDao().update(Sale(sale.id, newItem, newPrice))
                    withContext(Dispatchers.Main) {
                        setupRecyclerView()
                        dialog.dismiss()
                    }
                }
            }
        }

        dialog.show()
    }

    private fun deleteSale(sale: Sale) {
        CoroutineScope(Dispatchers.IO).launch {
            database.saleDao().delete(sale)
            withContext(Dispatchers.Main) {
                setupRecyclerView()
            }
        }
    }
}
