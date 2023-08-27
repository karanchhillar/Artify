package com.example.aritify.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.aritify.databinding.ItemMyChartBinding
import com.example.aritify.databinding.OrderPaymentListBinding
import com.example.aritify.dataclasses.PlaceOrder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class CartAdapter : RecyclerView.Adapter<Order2ViewHolder>(){

    var itemList = PlaceOrder()
    var itemTotal_price = MutableLiveData<Int>(0)
//    var itemTotal_quantity = MutableLiveData<Int>(0)
    private lateinit var binding : ItemMyChartBinding

    fun setItemList2(item : PlaceOrder){
        itemList = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Order2ViewHolder {
        binding = ItemMyChartBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return  Order2ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.price.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: Order2ViewHolder, position: Int) {
        val n = itemList.price.size - position -1

        val currentProductId : String = itemList.product_id[n]
        val currentProductName : String = itemList.product_name[n]
        val currentproductImage : String = itemList.product_image[n]
        val currentproductDetails: String? = itemList.product_description[n]
        val currentproductPrice: Int = itemList.price[n]
        val currentproductQuantity: String = itemList.available_quantity[n]
        val currentSellerId : String = itemList.seller_id[n]

        val currentValue = itemTotal_price.value ?: 0
        var change = 0
//        val currentValue2 = itemTotal_quantity.value ?: 1

//        val temp = itemTotal_price.value// when user delete the product
        itemTotal_price.value = currentValue + (currentproductPrice*currentproductQuantity.toString().toInt())
//        itemTotal_quantity.value = currentValue2 +

        holder.productName.text = currentProductName
        holder.productDetails.text = currentproductDetails
        holder.quantity.text = currentproductQuantity
        holder.price.text = "Price : ₹$currentproductPrice"
        Picasso.get().load(currentproductImage).into(holder.productImage)

        holder.plus_button.setOnClickListener {
            val quant = holder.quantity.text.toString().toInt() + 1
//            change = change + currentproductPrice
//            itemTotal_price.value = change
//            itemTotal_price.value =  currentproductPrice
            holder.quantity.text = quant.toString()
            notifyDataSetChanged()
            holder.change_quantity(currentProductId , quant)
        }
        holder.minus_button.setOnClickListener {
            var quant = holder.quantity.text.toString().toInt() - 1
            if(quant == 0){
                Toast.makeText(holder.itemView.context, "Quantity cannot be less than 1", Toast.LENGTH_SHORT).show()
                quant = 1
                return@setOnClickListener
            }
            notifyDataSetChanged()
//            change = change - currentproductPrice
//            itemTotal_price.value = change
//            itemTotal_price.value = currentproductPrice*(currentproductQuantity.toString().toInt()-1)
//            itemTotal_price.value = currentproductPrice
            holder.quantity.text = quant.toString()
            holder.change_quantity(currentProductId , quant)
        }
        holder.delete_button.setOnClickListener {
//            itemTotal_price.value = temp
            holder.delete_product(currentProductId)
        }
    }

}
class Order2ViewHolder(binding: ItemMyChartBinding) : RecyclerView.ViewHolder(binding.root){
    val productImage = binding.cartProductPhoto
    val productName = binding.cartProductName
    val productDetails = binding.cartProductDesc
    val price = binding.cartProductPrice
    val quantity = binding.cartProductQuantityText
    val plus_button = binding.rlProductPlus1
    val minus_button = binding.rlProductMinus1
    val delete_button = binding.productDeleteId

    fun change_quantity( product_id : String , quant : Int) {
        val firestore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val itemAddRef = firestore.collection("Cart").document(auth.currentUser?.uid.toString())

        itemAddRef.get().addOnSuccessListener {
            if (it.exists()) {
                val productId = it.get("product_id") as List<*>
                val newProductID = productId.toMutableList()

                if (newProductID.contains(product_id)) {
                    val index = newProductID.indexOf(product_id)
                    val setQuantity = it.get("available_quantity") as List<*>
                    val newSetQuantity = setQuantity.toMutableList()
                    val value = quant
                    newSetQuantity[index] = value.toString()

                    itemAddRef.update(hashMapOf("available_quantity" to newSetQuantity) as Map<String, Any>)
                        .addOnFailureListener {
                            Toast.makeText(itemView.context, "${it.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
        }
    }

    fun delete_product(product_id_req: String) {
        val firestore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val itemAddRef = firestore.collection("Cart").document(auth.currentUser?.uid.toString())
        itemAddRef.get().addOnSuccessListener {
            val productId = it.get("product_id") as List<*>
            val newProductID = productId.toMutableList()
            val index = newProductID.indexOf(product_id_req)
            newProductID.removeAt(index)

            val productName = it.get("product_name") as List<*>
            val newProductName = productName.toMutableList()
            newProductName.removeAt(index)

            val productDetails = it.get("product_description") as List<*>
            val newProductDetails = productDetails.toMutableList()
            newProductDetails.removeAt(index)

            val productPrice = it.get("price") as List<*>
            val newPrice = productPrice.toMutableList()
            newPrice.removeAt(index)

            val setQuantity = it.get("available_quantity") as List<*>
            val newSetQuantity = setQuantity.toMutableList()
            newSetQuantity.removeAt(index)

            val sellerId = it.get("seller_id") as List<*>
            val newSellerID = sellerId.toMutableList()
            newSellerID.removeAt(index)

            val productImage = it.get("product_image") as List<*>
            val newProductImage = productImage.toMutableList()
            newProductImage.removeAt(index)

            itemAddRef.update(hashMapOf(
                "product_id" to newProductID,
                "product_name" to newProductName,
                "product_description" to newProductDetails,
                "price" to newPrice,
                "available_quantity" to newSetQuantity,
                "seller_id" to newSellerID,
                "product_image" to newProductImage
            ) as Map<String, Any>).addOnSuccessListener {
                Toast.makeText(itemView.context, "Deleted Product", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(itemView.context, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }


}