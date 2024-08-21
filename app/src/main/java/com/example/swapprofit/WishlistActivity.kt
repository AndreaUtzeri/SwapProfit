package com.example.swapprofit

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.swapprofit.databinding.ActivityWishlistBinding

class WishlistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWishlistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSaveWishlist.setOnClickListener {
            val item = binding.etWishlistItem.text.toString()
            val price = binding.etWishlistPrice.text.toString().toDoubleOrNull()
            val link = binding.etWishlistLink.text.toString()

            if (item.isNotBlank() && price != null && link.isNotBlank()) {
                // wishlist logica
                Toast.makeText(this, "Wishlist item saved: $item for €$price", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter valid item, price, and link", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
