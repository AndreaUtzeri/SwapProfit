package com.example.swapprofit

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
import com.example.swapprofit.databinding.ActivityWishlistBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WishlistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWishlistBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        setupSpinner()

        setupRecyclerView()

        binding.btnSaveWishlist.setOnClickListener {
            val item = binding.etWishlistItem.text.toString()
            val price = binding.etWishlistPrice.text.toString().toDoubleOrNull()
            val link = binding.etWishlistLink.text.toString()

            if (item.isNotBlank() && price != null && link.isNotBlank()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val wishlistItem = WishlistItem(item = item, price = price, link = link)
                    database.wishlistDao().insert(wishlistItem)
                    setupRecyclerView()

                    // Log dei dati salvati
                    logSavedData()
                }

                Toast.makeText(this, "Wishlist item saved: $item for €$price", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter valid item, price, and link", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logSavedData() {
        CoroutineScope(Dispatchers.IO).launch {
            val wishlistItems = database.wishlistDao().getAll()
            Log.d("WishlistActivity", "Saved Wishlist Items: ${wishlistItems.map { "${it.item}: €${it.price}, Link: ${it.link}" }}")
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
            val wishlist = when (sortOption) {
                1 -> database.wishlistDao().getAll().sortedBy { it.id }  // Sort by Date (Oldest)
                2 -> database.wishlistDao().getAll().sortedBy { it.price }  // Sort by Price (Low to High)
                3 -> database.wishlistDao().getAll().sortedByDescending { it.price }  // Sort by Price (High to Low)
                else -> database.wishlistDao().getAll().sortedByDescending { it.id }  // Sort by Date (Newest)
            }
            withContext(Dispatchers.Main) {
                binding.rvWishlistList.layoutManager = LinearLayoutManager(this@WishlistActivity)
                binding.rvWishlistList.adapter = WishlistItemAdapter(
                    wishlist,
                    ::editWishlistItem,
                    ::deleteWishlistItem
                )
            }
        }
    }

    private fun editWishlistItem(wishlistItem: WishlistItem) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_wishlist, null)
        builder.setView(dialogView)
        val dialog = builder.create()

        val etItemName = dialogView.findViewById<EditText>(R.id.etItemName)
        val etItemPrice = dialogView.findViewById<EditText>(R.id.etItemPrice)
        val etItemLink = dialogView.findViewById<EditText>(R.id.etItemLink)

        etItemName.setText(wishlistItem.item)
        etItemPrice.setText(wishlistItem.price.toString())
        etItemLink.setText(wishlistItem.link)

        dialogView.findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            val newItem = etItemName.text.toString()
            val newPrice = etItemPrice.text.toString().toDoubleOrNull()
            val newLink = etItemLink.text.toString()

            if (newItem.isNotBlank() && newPrice != null && newLink.isNotBlank()) {
                CoroutineScope(Dispatchers.IO).launch {
                    database.wishlistDao().update(WishlistItem(wishlistItem.id, newItem, newPrice, newLink))
                    withContext(Dispatchers.Main) {
                        setupRecyclerView()
                        dialog.dismiss()
                    }
                }
            }
        }

        dialog.show()
    }
    private fun deleteWishlistItem(wishlistItem: WishlistItem) {
        CoroutineScope(Dispatchers.IO).launch {
            database.wishlistDao().delete(wishlistItem)
            withContext(Dispatchers.Main) {
                setupRecyclerView()
            }
        }
    }

}
