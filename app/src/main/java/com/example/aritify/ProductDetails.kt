package com.example.aritify

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.aritify.cart.PaymentScreen
import com.example.aritify.databinding.ActivityProductDetailsBinding
import com.example.aritify.dataclasses.AllProductsData
import com.example.aritify.dataclasses.PlaceOrder
import com.example.aritify.mvvm.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ProductDetails : AppCompatActivity() {

    private lateinit var binding : ActivityProductDetailsBinding
    private lateinit var vm : ViewModel
    private lateinit var auth : FirebaseAuth
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
//        vm = ViewModelProvider(this,viewModelFactory)[ViewModel::class.java]


        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.transparent)

//        val intent = Intent()
        val currentProductName : String? = intent.getStringExtra("currentProductName")
        val currentProductImage : String? = intent.getStringExtra("currentProductImage")
        val currentProductDetails: String? = intent.getStringExtra("currentProductDetails")
        val currentProductPrice: Int = intent.getIntExtra("currentProductPrice" , 0)
        val currentProductQuantity: String? = intent.getStringExtra("currentProductQuantity")
        val currentSellerId: String? = intent.getStringExtra("currentSellerId")
        val productID : String? = intent.getStringExtra("currentProductId")


        binding.tvproductname1.text = currentProductName
        binding.tvproductPrice.text = "₹$currentProductPrice"
        binding.tvproductdesc.text = currentProductDetails
        Picasso.get().load(currentProductImage).into(binding.imgproduct)


        binding.imgplusicon.setOnClickListener {

            val curr_val = binding.setQuantity.text.toString().toInt()
            if (curr_val == currentProductQuantity.toString().toInt()){
                Toast.makeText(this, "Can't Buy More", Toast.LENGTH_SHORT).show()
            }
            else{
                binding.setQuantity.text = (curr_val+1).toString()
            }
        }
        binding.imgminusicon.setOnClickListener {
            val curr_val = binding.setQuantity.text.toString().toInt()
            if (curr_val == 1){
                Toast.makeText(this, "Can't Be Less Then 0", Toast.LENGTH_SHORT).show()
            }
            else{
                binding.setQuantity.text = (curr_val-1).toString()
            }
        }

//        binding.efaAddToCart.setOnClickListener {
//            auth = FirebaseAuth.getInstance()
//
//            val cart =AllProductsData(
//                listOf(productID!!)
//                listOf(!!)
//            )
//        }

        binding.efaAddToCart.setOnClickListener{
            val firestore = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val itemAddRef = firestore.collection("Cart").document(auth.currentUser?.uid.toString())
            if (binding.efaAddToCart.text == "Add To Cart"){
                Toast.makeText(this@ProductDetails, "${binding.efaAddToCart.text}", Toast.LENGTH_SHORT).show()
                itemAddRef.get().addOnSuccessListener {
                    if (it.exists()) {
                        val productId = it.get("product_id") as List<*>
                        val newProductID = productId.toMutableList()

                        if(newProductID.contains(productID)) {
                            val index = newProductID.indexOf(productID)
                            val setQuantity = it.get("available_quantity") as List<*>
                            val newSetQuantity = setQuantity.toMutableList()
                            val value = newSetQuantity[index].toString().toInt() + binding.setQuantity.text.toString().toInt()
                            newSetQuantity[index] = value.toString()

                            itemAddRef.update(hashMapOf("available_quantity" to newSetQuantity) as Map<String, Any>)
                                .addOnSuccessListener {
                                    binding.efaAddToCart.text = "Remove From Cart"
                                    Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        this@ProductDetails,
                                        "${it.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }

                        else {
                            newProductID.add(productID)

                            val productName = it.get("product_name") as List<*>
                            val newProductName = productName.toMutableList()
                            newProductName.add(currentProductName)

                            val productDetails = it.get("product_description") as List<*>
                            val newProductDetails = productDetails.toMutableList()
                            newProductDetails.add(currentProductDetails)

                            val productPrice = it.get("price") as List<*>
                            val newPrice = productPrice.toMutableList()
                            newPrice.add(currentProductPrice)

                            val setQuantity = it.get("available_quantity") as List<*>
                            val newSetQuantity = setQuantity.toMutableList()
                            newSetQuantity.add(binding.setQuantity.text.toString())

                            val sellerId = it.get("seller_id") as List<*>
                            val newSellerID = sellerId.toMutableList()
                            newSellerID.add(currentSellerId)

                            val productImage = it.get("product_image") as List<*>
                            val newProductImage = productImage.toMutableList()
                            newProductImage.add(currentProductImage)


                            itemAddRef.update(
                                hashMapOf(
                                    "product_id" to newProductID,
                                    "product_name" to newProductName,
                                    "product_description" to newProductDetails,
                                    "price" to newPrice,
                                    "available_quantity" to newSetQuantity,
                                    "seller_id" to newSellerID,
                                    "product_image" to newProductImage
                                ) as Map<String, Any>
                            ).addOnSuccessListener {
                                binding.efaAddToCart.text = "Remove From Cart"
                                Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
                            }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        this@ProductDetails,
                                        "${it.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    } else {
                        val itemData = hashMapOf(
                            "product_id" to listOf(productID),
                            "product_name" to listOf(currentProductName),
                            "product_description" to listOf(currentProductDetails),
                            "price" to listOf(currentProductPrice),
                            "available_quantity" to listOf(binding.setQuantity.text.toString()),
                            "seller_id" to listOf(currentSellerId),
                            "product_image" to listOf(currentProductImage),
                        )
                        itemAddRef.set(itemData).addOnSuccessListener {
                            binding.efaAddToCart.text = "Remove From Cart"
                            Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener {
                                Toast.makeText(this@ProductDetails, "${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
            else{
                itemAddRef.get().addOnSuccessListener {
                    val productId = it.get("product_id") as List<*>
                    val newProductID = productId.toMutableList()
                    val index = newProductID.indexOf(productID)
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
                        binding.efaAddToCart.text = "Add To Cart"
                        Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
                    }
                        .addOnFailureListener {
                            Toast.makeText(this@ProductDetails, "${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        binding.extendedFloatingActionButton2.setOnClickListener {

            val data = PlaceOrder(
                mutableListOf(productID!!),
                mutableListOf(currentProductName!!),
                mutableListOf(currentProductPrice!!),
                mutableListOf(currentProductDetails!!),
                mutableListOf(currentProductImage!!),
                mutableListOf(binding.setQuantity.text.toString()),
                mutableListOf(currentSellerId!!)
            )

            val intent = Intent(this , PaymentScreen::class.java)
            intent.putExtra("single_order_detail" , data)
            startActivity(intent)

        }
    }

}