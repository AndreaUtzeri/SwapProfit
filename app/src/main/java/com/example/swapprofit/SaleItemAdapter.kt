package com.example.swapprofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SaleItemAdapter(private val salesList: List<Sale>) :
    RecyclerView.Adapter<SaleItemAdapter.SaleItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sale, parent, false)
        return SaleItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: SaleItemViewHolder, position: Int) {
        val saleItem = salesList[position]
        holder.itemName.text = saleItem.item
        holder.itemPrice.text = "€${saleItem.price}"
    }

    override fun getItemCount(): Int {
        return salesList.size
    }

    class SaleItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.tvItemName)
        val itemPrice: TextView = view.findViewById(R.id.tvItemPrice)
    }
}
