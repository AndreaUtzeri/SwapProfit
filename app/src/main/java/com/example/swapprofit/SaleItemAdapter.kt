package com.example.swapprofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SaleItemAdapter(
    private val salesList: List<Sale>,
    private val onEdit: (Sale) -> Unit,
    private val onDelete: (Sale) -> Unit
) : RecyclerView.Adapter<SaleItemAdapter.SaleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sale, parent, false)
        return SaleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {
        val sale = salesList[position]
        holder.itemName.text = sale.item
        holder.itemPrice.text = "€${sale.price}"

        holder.btnEdit.setOnClickListener { onEdit(sale) }
        holder.btnDelete.setOnClickListener { onDelete(sale) }
    }

    override fun getItemCount(): Int {
        return salesList.size
    }

    class SaleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.tvItemName)
        val itemPrice: TextView = view.findViewById(R.id.tvItemPrice)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }
}
