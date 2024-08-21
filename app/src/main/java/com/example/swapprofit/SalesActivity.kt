package com.example.swapprofit

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.swapprofit.databinding.ActivitySalesBinding

class SalesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySalesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSaveSale.setOnClickListener {
            val item = binding.etSaleItem.text.toString()
            val price = binding.etSalePrice.text.toString().toDoubleOrNull()

            if (item.isNotBlank() && price != null) {
                // logica di sale
                Toast.makeText(this, "Sale saved: $item for €$price", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter valid item and price", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
