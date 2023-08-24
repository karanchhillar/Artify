package com.example.aritify.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aritify.ProductDetails
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

//        val position = getAdapterPosition();
        if(n != RecyclerView.NO_POSITION){
            // you can trust the adapter position
            // do whatever you intend to do with this position

            val currentProductName : String = itemList.product_name[n]
            val currentproductImage : String = itemList.product_image[n]
            val currentproductDetails: String = itemList.product_description[n]
            val currentproductPrice: Int = itemList.price[n]

            holder.productName.text = currentProductName
            holder.productDetails.text = currentproductDetails
            holder.price.text = "₹ $currentproductPrice"
            Picasso.get().load(currentproductImage).into(holder.productImage)

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context , ProductDetails::class.java)
                intent.putExtra("currentProductName" , currentProductName)
                intent.putExtra("currentProductImage" , currentproductImage)
                intent.putExtra("currentProductDetails" , currentproductDetails)
                intent.putExtra("currentProductPrice" , currentproductPrice)
                intent.putExtra("currentProductQuantity" , itemList.available_quantity[n])
                intent.putExtra("currentSellerId" , itemList.seller_id[n])
                intent.putExtra("currentProductId" , itemList.product_id[n])


                holder.itemView.context.startActivity(intent)
            }
        }


    }
}

class ItemViewHolder(binding: ProductsItemListBinding) : RecyclerView.ViewHolder(binding.root){
    val productImage = binding.productImage
    val productName = binding.productName
    val productDetails = binding.productDescription
    val price = binding.productPrice
//    val wishlist = binding.whislist
}