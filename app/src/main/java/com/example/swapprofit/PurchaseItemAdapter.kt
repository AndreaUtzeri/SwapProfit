package com.example.swapprofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PurchaseItemAdapter(private val purchasesList: List<Purchase>) :
    RecyclerView.Adapter<PurchaseItemAdapter.PurchaseItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_purchase, parent, false)
        return PurchaseItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: PurchaseItemViewHolder, position: Int) {
        val purchaseItem = purchasesList[position]
        holder.itemName.text = purchaseItem.item
        holder.itemPrice.text = "€${purchaseItem.price}"
    }

    override fun getItemCount(): Int {
        return purchasesList.size
    }

    class PurchaseItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.tvItemName)
        val itemPrice: TextView = view.findViewById(R.id.tvItemPrice)
    }
}
