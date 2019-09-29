package com.example.myrestaurantapp.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myrestaurantapp.R
import com.example.myrestaurantapp.helpers.APIConstants
import com.example.myrestaurantapp.models.Item
import com.example.myrestaurantapp.ui.MainActivity

class ItemsAdapter(var items: MutableList<Item>, val activity: MainActivity) :
    RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cell, parent, false)

        return ItemsViewHolder(view)

    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {

        val item = items[position]
        holder.nameTextView.text = item.name
        holder.priceTextView.text = "${item.price} €"
        holder.typeTextView.text = item.type
        Glide.with(activity).load(APIConstants.imageItemURL + item.photo_url)
            .into(holder.photoImageView)
    }


    inner class ItemsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var photoImageView: ImageView = view.findViewById(R.id.itemPhotoImageView)
        var nameTextView: TextView = view.findViewById(R.id.itemNameTextView)
        var typeTextView: TextView = view.findViewById(R.id.itemTypeTextView)
        var priceTextView: TextView = view.findViewById(R.id.itemPriceTextView)
    }
}
