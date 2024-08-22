package com.example.swapprofit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        binding.btnSaveWishlist.setOnClickListener {
            val item = binding.etWishlistItem.text.toString()
            val price = binding.etWishlistPrice.text.toString().toDoubleOrNull()
            val link = binding.etWishlistLink.text.toString()

            if (item.isNotBlank() && price != null && link.isNotBlank()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val wishlistItem = WishlistItem(item = item, price = price, link = link)
                    database.wishlistDao().insert(wishlistItem)

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
}
