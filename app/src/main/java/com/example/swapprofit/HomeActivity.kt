package com.example.swapprofit

import android.graphics.Color
import android.util.Log
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.swapprofit.databinding.ActivityHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        binding.btnSales.setOnClickListener {
            val intent = Intent(this, SalesActivity::class.java)
            startActivity(intent)
        }

        binding.btnPurchases.setOnClickListener {
            val intent = Intent(this, PurchasesActivity::class.java)
            startActivity(intent)
        }

        binding.btnWishlist.setOnClickListener {
            val intent = Intent(this, WishlistActivity::class.java)
            startActivity(intent)
        }

        // Update counters
        updateCounters()
    }

    override fun onResume() {
        super.onResume()
        updateCounters()  //Per aggiornare quando si torna nella homeactivity, senza questo i counter si aggiornano solo riavviando l'app.
    }

    private fun updateCounters() {
        CoroutineScope(Dispatchers.IO).launch {
            val sales = database.saleDao().getAll()
            val purchases = database.purchaseDao().getAll()
            val wishlist = database.wishlistDao().getAll()

            val totalSales = sales.sumOf { it.price }
            val totalPurchases = purchases.sumOf { it.price }
            val totalWishlist = wishlist.sumOf { it.price }

            Log.d("HomeActivity", "Total Sales: $totalSales, Total Purchases: $totalPurchases, Total Wishlist: $totalWishlist")

            withContext(Dispatchers.Main) {
                val counter1Value = totalSales - totalPurchases
                val counter2Value = totalSales - totalPurchases - totalWishlist


                binding.tvCounter1.text = "Profit: €$counter1Value"
                binding.tvCounter2.text = "Future Profit: €$counter2Value"

                // Cambia il colore del testo in base al valore
                if (counter1Value < 0) {
                    binding.tvCounter1.setTextColor(Color.RED)
                } else {
                    binding.tvCounter1.setTextColor(Color.GREEN)
                }

                if (counter2Value < 0) {
                    binding.tvCounter2.setTextColor(Color.RED)
                } else {
                    binding.tvCounter2.setTextColor(Color.GREEN)
                }
            }
        }
    }
}
