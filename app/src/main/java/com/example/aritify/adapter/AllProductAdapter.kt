package com.example.aritify.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aritify.databinding.ActivityAllProductBinding
import com.example.aritify.databinding.ProductsItemListBinding
import com.example.aritify.dataclasses.AllProductsData
import com.squareup.picasso.Picasso

class AllProductAdapter : RecyclerView.Adapter<ItemViewHolder>() {

    var itemList = AllProductsData()
    private lateinit var binding: ProductsItemListBinding

    fun setItemList1(item : AllProductsData){
        itemList = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        binding = ProductsItemListBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return  ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.price.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val n = itemList.price.size - position -1

        val currentProductName : String = itemList.productName[n]
        val currentproductImage : String = itemList.productImage[n]
        val currentproductDetails: String = itemList.productDescription[n]
        val currentproductPrice: Int = itemList.price[n]

        holder.productName.text = currentProductName
        holder.productDetails.text = currentproductDetails
        holder.price.text = "₹ $currentproductPrice"
        Picasso.get().load(currentproductImage).into(holder.productImage)
    }
}

class ItemViewHolder(binding: ProductsItemListBinding) : RecyclerView.ViewHolder(binding.root){
    val productImage = binding.productImage
    val productName = binding.productName
    val productDetails = binding.productDescription
    val price = binding.productPrice
//    val wishlist = binding.whislist
}