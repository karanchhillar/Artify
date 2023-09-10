package com.example.aritify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aritify.databinding.OrderPlacedItemBinding
import com.example.aritify.dataclasses.OrderDetail
import com.example.aritify.dataclasses.ShowsData

class YourOrderAdapter  : RecyclerView.Adapter<YourOrderViewHolder>()
{
    var itemList = OrderDetail()
    private lateinit var binding: OrderPlacedItemBinding

    fun setItemList1(item : OrderDetail){
        itemList = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YourOrderViewHolder {
        binding = OrderPlacedItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return  YourOrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.product_id.size
    }

    override fun onBindViewHolder(holder: YourOrderViewHolder, position: Int) {
        val n = itemList.product_id.size - position - 1

//        val currentproductPrice: Int = itemList.price

        holder.textViewName.text = itemList.rec_name[n].toString()
        holder.textViewPhone.text = itemList.rec_phone[n].toString()
        holder.textViewAddress.text = itemList.address[n].toString()
        holder.textViewQuantity.text = itemList.product_quantity[n]
        holder.textViewPrice.text = itemList.price.toString()
    }
}

class YourOrderViewHolder(binding: OrderPlacedItemBinding) : RecyclerView.ViewHolder(binding.root) {
    val textViewName = binding.textViewName
    val textViewAddress = binding.textViewAddress
    val textViewPhone = binding.textViewPhone
    val textViewQuantity = binding.textViewQuantity
    val textViewPrice = binding.textViewPrice
}
