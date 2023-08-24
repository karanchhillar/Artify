package com.example.aritify.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aritify.databinding.OrderPaymentListBinding
import com.example.aritify.dataclasses.PlaceOrder
import com.squareup.picasso.Picasso

class CartAdapter: RecyclerView.Adapter<OrderViewHolder>()  {

    var itemList = PlaceOrder()
    private lateinit var binding : OrderPaymentListBinding

    fun setItemList1(item : PlaceOrder){
        itemList = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        binding = OrderPaymentListBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return  OrderViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val n = itemList.price.size - position -1

        val currentProductName : String = itemList.product_name[n]
        val currentproductImage : String = itemList.product_image[n]
        val currentproductDetails: String = itemList.product_description[n]
        val currentproductPrice: Int = itemList.price[n]
        val currentproductQuantity: String = itemList.available_quantity[n]

        holder.productName.text = currentProductName
        holder.productDetails.text = currentproductDetails
        holder.quantity.text = currentproductQuantity
        holder.price.text = "Price : ₹$currentproductPrice"
        Picasso.get().load(currentproductImage).into(holder.productImage)
    }

    override fun getItemCount(): Int {
        return itemList.price.size
    }

}

class OrderViewHolder(binding: OrderPaymentListBinding) : RecyclerView.ViewHolder(binding.root){
    val productImage = binding.orderImg
    val productName = binding.productname
    val productDetails = binding.productDisc
    val price = binding.orderPrice
    val quantity = binding.orderQuantity
}