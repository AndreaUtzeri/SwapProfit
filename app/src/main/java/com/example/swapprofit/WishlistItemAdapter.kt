package com.example.swapprofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WishlistItemAdapter(private val wishlist: List<WishlistItem>) :
    RecyclerView.Adapter<WishlistItemAdapter.WishlistItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wishlist, parent, false)
        return WishlistItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishlistItemViewHolder, position: Int) {
        val wishlistItem = wishlist[position]
        holder.itemName.text = wishlistItem.item
        holder.itemPrice.text = "€${wishlistItem.price}"
        holder.itemLink.text = wishlistItem.link
    }

    override fun getItemCount(): Int {
        return wishlist.size
    }

    class WishlistItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.tvItemName)
        val itemPrice: TextView = view.findViewById(R.id.tvItemPrice)
        val itemLink: TextView = view.findViewById(R.id.tvItemLink)
    }
}
