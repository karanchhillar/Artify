package com.example.aritify.mvvm

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aritify.MyApplication
import com.example.aritify.Utils
import com.example.aritify.dataclasses.AllProductsData
import com.example.aritify.dataclasses.ArtifyUser
import com.example.aritify.dataclasses.OrderDetail
import com.example.aritify.dataclasses.PlaceOrder
import com.example.aritify.dataclasses.ShowsData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel : ViewModel() {

    private lateinit var database : FirebaseDatabase
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    init {
        retrive_item_data()
        retrive_cart_data()
        retrive_Best_item_data()
    }

    fun upload_user_data(user : ArtifyUser){
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firestore.collection("artify_user").document(auth.currentUser?.uid.toString()).set(user)
    }

    fun placeOrder(order : OrderDetail) {
        database = FirebaseDatabase.getInstance()
        database.reference.child("ORDER DEATILS").child(Utils.getUidLoggedIn())
            .child(Utils.getTime().toString()).setValue(order)
            .addOnSuccessListener {
                Toast.makeText(MyApplication.getAppContext(), "YOUR ORDER IS UPLOADED \n ", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(MyApplication.getAppContext(), "FACING PROBLEM RIGHT NOW", Toast.LENGTH_SHORT).show()
            }
    }

    val myHomeItem = MutableLiveData<AllProductsData>()
    fun retrive_Best_item_data() =viewModelScope.launch (Dispatchers.IO){
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        try {
            firestore.collection("Paid_Promotions").document("products")
                .addSnapshotListener{value , error->

                    if (error != null){
                        return@addSnapshotListener
                    }
                    if (value!!.exists()) {
                        val itemData = value.toObject(AllProductsData::class.java)
                        myHomeItem.value = itemData!!
                    }
                }
        }catch (_: Exception){}
    }

//    val myShowItem = MutableLiveData<ShowsData>()
    fun retrive_show_data(category : String) :MutableLiveData<ShowsData> {
        val myShowItem = MutableLiveData<ShowsData>()
        viewModelScope.launch(Dispatchers.IO)
        {
            firestore = FirebaseFirestore.getInstance()
            auth = FirebaseAuth.getInstance()
            try {
                firestore.collection("Shows").document(category)
                    .addSnapshotListener { value, error ->

                        if (error != null) {
                            return@addSnapshotListener
                        }
                        if (value!!.exists()) {
                            val itemData = value.toObject(ShowsData::class.java)
                            myShowItem.value = itemData!!
                        }
                    }
            } catch (_: Exception) {
            }
        }
        return myShowItem
    }

    val myItem = MutableLiveData<AllProductsData>()
    fun retrive_item_data() =viewModelScope.launch (Dispatchers.IO){
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        try {
            firestore.collection("All_Products").document("All_Products")
                .addSnapshotListener{value , error->

                    if (error != null){
                        return@addSnapshotListener
                    }
                    if (value!!.exists()) {
                        val itemData = value.toObject(AllProductsData::class.java)
                        myItem.value = itemData!!
                    }
                }
        }catch (_: Exception){}
    }

    val myCatItem = MutableLiveData<AllProductsData>()
    fun retrive_item_data_category(collectionName : String) =viewModelScope.launch (Dispatchers.IO){
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        try {
            firestore.collection("Category").document(collectionName)
                .addSnapshotListener{value , error->

                    if (error != null){
                        return@addSnapshotListener
                    }
                    if (value!!.exists()) {
                        val itemData = value.toObject(AllProductsData::class.java)
                        myCatItem.value = itemData!!
                    }
                }
        }catch (_: Exception){}
    }

    val myCartItem = MutableLiveData<PlaceOrder>()
    fun retrive_cart_data() =viewModelScope.launch (Dispatchers.IO){
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        try {
            firestore.collection("Cart").document(auth.currentUser?.uid.toString())
                .addSnapshotListener{value , error->

                    if (error != null){
                        return@addSnapshotListener
                    }
                    if (value!!.exists()) {
                        val itemData = value.toObject(PlaceOrder::class.java)
                        myCartItem.value = itemData!!
                    }
                }
        }catch (_: Exception){}
    }

    fun retrive_user_data(callback: (ArtifyUser) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        firestore.collection("artify_user").document(auth.currentUser?.uid.toString()).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val value = documentSnapshot.toObject(ArtifyUser::class.java)
                callback(value!!)
            }
        }
    }

//    fun upload_cart_data(text : String , productID : String , quantity : String){
//
//        val firestore = FirebaseFirestore.getInstance()
//        val auth = FirebaseAuth.getInstance()
//        val itemAddRef = firestore.collection("Cart").document(auth.currentUser?.uid.toString())
//        if (text == "Add To Cart"){
//            Toast.makeText(MyApplication.getAppContext(), text, Toast.LENGTH_SHORT).show()
//            itemAddRef.get().addOnSuccessListener {
//                if (it.exists()) {
//                    val productId = it.get("product_id") as List<*>
//                    val newProductID = productId.toMutableList()
//
//                    if(newProductID.contains(productID)) {
//                        val index = newProductID.indexOf(productID)
//                        val setQuantity = it.get("available_quantity") as List<*>
//                        val newSetQuantity = setQuantity.toMutableList()
//                        val value = newSetQuantity[index].toString().toInt() + quantity.toInt()
//                        newSetQuantity[index] = value.toString()
//
//                        itemAddRef.update(hashMapOf("available_quantity" to newSetQuantity) as Map<String, Any>)
//                            .addOnSuccessListener {
//                                binding.efaAddToCart.text = "Remove From Cart"
//                                Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
//                            }
//                            .addOnFailureListener {
//                                Toast.makeText(
//                                    MyApplication.getAppContext(),
//                                    "${it.message}",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                    }
//
//                    else {
//                        newProductID.add(productID)
//
//                        val productName = it.get("product_name") as List<*>
//                        val newProductName = productName.toMutableList()
//                        newProductName.add(currentProductName)
//
//                        val productDetails = it.get("product_description") as List<*>
//                        val newProductDetails = productDetails.toMutableList()
//                        newProductDetails.add(currentProductDetails)
//
//                        val productPrice = it.get("price") as List<*>
//                        val newPrice = productPrice.toMutableList()
//                        newPrice.add(currentProductPrice)
//
//                        val setQuantity = it.get("available_quantity") as List<*>
//                        val newSetQuantity = setQuantity.toMutableList()
//                        newSetQuantity.add(binding.setQuantity.text.toString())
//
//                        val sellerId = it.get("seller_id") as List<*>
//                        val newSellerID = sellerId.toMutableList()
//                        newSellerID.add(currentSellerId)
//
//                        val productImage = it.get("product_image") as List<*>
//                        val newProductImage = productImage.toMutableList()
//                        newProductImage.add(currentProductImage)
//
//
//                        itemAddRef.update(
//                            hashMapOf(
//                                "product_id" to newProductID,
//                                "product_name" to newProductName,
//                                "product_description" to newProductDetails,
//                                "price" to newPrice,
//                                "available_quantity" to newSetQuantity,
//                                "seller_id" to newSellerID,
//                                "product_image" to newProductImage
//                            ) as Map<String, Any>
//                        ).addOnSuccessListener {
//                            binding.efaAddToCart.text = "Remove From Cart"
//                            Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
//                        }
//                            .addOnFailureListener {
//                                Toast.makeText(
//                                    this@ProductDetails,
//                                    "${it.message}",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                    }
//                } else {
//                    val itemData = hashMapOf(
//                        "product_id" to listOf(productID),
//                        "product_name" to listOf(currentProductName),
//                        "product_description" to listOf(currentProductDetails),
//                        "price" to listOf(currentProductPrice),
//                        "available_quantity" to listOf(binding.setQuantity.text.toString()),
//                        "seller_id" to listOf(currentSellerId),
//                        "product_image" to listOf(currentProductImage),
//                    )
//                    itemAddRef.set(itemData).addOnSuccessListener {
//                        binding.efaAddToCart.text = "Remove From Cart"
//                        Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
//                    }
//                        .addOnFailureListener {
//                            Toast.makeText(this@ProductDetails, "${it.message}", Toast.LENGTH_SHORT).show()
//                        }
//                }
//            }
//        }
//        else{
//            itemAddRef.get().addOnSuccessListener {
//                val productId = it.get("product_id") as List<*>
//                val newProductID = productId.toMutableList()
//                val index = newProductID.indexOf(productID)
//                newProductID.removeAt(index)
//
//                val productName = it.get("product_name") as List<*>
//                val newProductName = productName.toMutableList()
//                newProductName.removeAt(index)
//
//                val productDetails = it.get("product_description") as List<*>
//                val newProductDetails = productDetails.toMutableList()
//                newProductDetails.removeAt(index)
//
//                val productPrice = it.get("price") as List<*>
//                val newPrice = productPrice.toMutableList()
//                newPrice.removeAt(index)
//
//                val setQuantity = it.get("available_quantity") as List<*>
//                val newSetQuantity = setQuantity.toMutableList()
//                newSetQuantity.removeAt(index)
//
//                val sellerId = it.get("seller_id") as List<*>
//                val newSellerID = sellerId.toMutableList()
//                newSellerID.removeAt(index)
//
//                val productImage = it.get("product_image") as List<*>
//                val newProductImage = productImage.toMutableList()
//                newProductImage.removeAt(index)
//
//                itemAddRef.update(hashMapOf(
//                    "product_id" to newProductID,
//                    "product_name" to newProductName,
//                    "product_description" to newProductDetails,
//                    "price" to newPrice,
//                    "available_quantity" to newSetQuantity,
//                    "seller_id" to newSellerID,
//                    "product_image" to newProductImage
//                ) as Map<String, Any>).addOnSuccessListener {
//                    binding.efaAddToCart.text = "Add To Cart"
//                    Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
//                }
//                    .addOnFailureListener {
//                        Toast.makeText(this@ProductDetails, "${it.message}", Toast.LENGTH_SHORT).show()
//                    }
//            }
//        }
//    }



}