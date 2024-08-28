package com.example.swapprofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PurchaseItemAdapter(
    private val purchasesList: List<Purchase>,
    private val onEditClick: (Purchase) -> Unit,
    private val onDeleteClick: (Purchase) -> Unit
) : RecyclerView.Adapter<PurchaseItemAdapter.PurchaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_purchase, parent, false)
        return PurchaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: PurchaseViewHolder, position: Int) {
        val purchase = purchasesList[position]
        holder.itemName.text = purchase.item
        holder.itemPrice.text = "€${purchase.price}"
        holder.btnEdit.setOnClickListener { onEditClick(purchase) }
        holder.btnDelete.setOnClickListener { onDeleteClick(purchase) }
    }

    override fun getItemCount(): Int {
        return purchasesList.size
    }

    class PurchaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.tvItemName)//?: throw NullPointerException("TextView with ID 'tvItemName' not found")
        val itemPrice: TextView = view.findViewById(R.id.tvItemPrice)//  ?: throw NullPointerException("TextView with ID 'tvItemPrice' not found")
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)//?: throw NullPointerException("ImageButton with ID 'btnEdit' not found")
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)// ?: throw NullPointerException("ImageButton with ID 'btnDelete' not found")
    }
}
