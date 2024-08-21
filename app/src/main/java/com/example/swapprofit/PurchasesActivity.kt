package com.example.swapprofit

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.swapprofit.databinding.ActivityPurchasesBinding

class PurchasesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPurchasesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchasesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSavePurchase.setOnClickListener {
            val item = binding.etPurchaseItem.text.toString()
            val price = binding.etPurchasePrice.text.toString().toDoubleOrNull()

            if (item.isNotBlank() && price != null) {
                // logica di purchase
                Toast.makeText(this, "Purchase saved: $item for €$price", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter valid item and price", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
