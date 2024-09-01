package com.example.swapprofit

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AnalyticsActivity : AppCompatActivity() {

    private lateinit var chart: LineChart
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        chart = findViewById(R.id.chart)
        database = AppDatabase.getDatabase(this)

        val addDataButton: Button = findViewById(R.id.addDataButton)
        addDataButton.setOnClickListener {
            addTestDataForOctober()
        }

        // Configura l'asse X del grafico
        val xAxis = chart.xAxis
        xAxis.valueFormatter = MonthValueFormatter(listOf()) 
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = 45f
        xAxis.setLabelCount(5, true)

        loadChartData()
    }

    private fun getStartOfMonth(year: Int, month: Int): Long {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.set(year, month, 1, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun addTestDataForOctober() {
        CoroutineScope(Dispatchers.IO).launch {
            val octoberStart = getStartOfMonth(2024, Calendar.OCTOBER)

            val octoberSale = Sale(item = "Item1", price = 200.0, dateAdded = octoberStart)
            val octoberPurchase = Purchase(item = "Item2", price = 150.0, dateAdded = octoberStart)

            database.saleDao().insert(octoberSale)
            database.purchaseDao().insert(octoberPurchase)

            withContext(Dispatchers.Main) {
                loadChartData() // Ricarica i dati nel grafico
            }
        }
    }

    private fun loadChartData() {
        CoroutineScope(Dispatchers.IO).launch {
            val sales = database.saleDao().getAll()
            val purchases = database.purchaseDao().getAll()

            // Per convertire i totali in mensili
            val salesMonthlyTotals = aggregateSalesByMonth(sales)
            val purchasesMonthlyTotals = aggregatePurchasesByMonth(purchases)


            val sortedMonths = salesMonthlyTotals.keys.union(purchasesMonthlyTotals.keys).toList().sorted()
            val salesEntries = sortedMonths.mapIndexed { index, month ->
                Entry(index.toFloat(), (salesMonthlyTotals[month] ?: 0.0).toFloat())
            }
            val purchasesEntries = sortedMonths.mapIndexed { index, month ->
                Entry(index.toFloat(), (purchasesMonthlyTotals[month] ?: 0.0).toFloat())
            }

            val salesDataSet = LineDataSet(salesEntries, "Sales")
            val purchasesDataSet = LineDataSet(purchasesEntries, "Purchases")

            salesDataSet.color = android.graphics.Color.GREEN
            salesDataSet.valueTextColor = android.graphics.Color.GREEN
            purchasesDataSet.color = android.graphics.Color.RED
            purchasesDataSet.valueTextColor = android.graphics.Color.RED


            salesDataSet.setDrawCircles(true)
            salesDataSet.setDrawValues(true)
            purchasesDataSet.setDrawCircles(true)
            purchasesDataSet.setDrawValues(true)

            val lineData = LineData(salesDataSet, purchasesDataSet)

            withContext(Dispatchers.Main) {
                chart.data = lineData
                chart.xAxis.valueFormatter = MonthValueFormatter(sortedMonths)
                chart.invalidate()
            }
        }
    }

    private fun aggregateSalesByMonth(data: List<Sale>): Map<String, Double> {
        val monthlyTotals = mutableMapOf<String, Double>()
        val dateFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        for (item in data) {
            val date = Date(item.dateAdded)
            val month = dateFormat.format(date)
            monthlyTotals[month] = monthlyTotals.getOrDefault(month, 0.0) + item.price
        }
        return monthlyTotals
    }

    private fun aggregatePurchasesByMonth(data: List<Purchase>): Map<String, Double> {
        val monthlyTotals = mutableMapOf<String, Double>()
        val dateFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        for (item in data) {
            val date = Date(item.dateAdded)
            val month = dateFormat.format(date)
            monthlyTotals[month] = monthlyTotals.getOrDefault(month, 0.0) + item.price
        }
        return monthlyTotals
    }

    private class MonthValueFormatter(private val months: List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return months.getOrElse(value.toInt()) { "" }
        }
    }
}
