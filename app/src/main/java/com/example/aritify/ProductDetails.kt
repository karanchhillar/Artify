package com.example.aritify

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.example.aritify.cart.PaymentScreen
import com.example.aritify.databinding.ActivityProductDetailsBinding
import com.example.aritify.databinding.ProductsItemListBinding
import com.example.aritify.dataclasses.PlaceOrder
import com.squareup.picasso.Picasso

class ProductDetails : AppCompatActivity() {

    private lateinit var binding : ActivityProductDetailsBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.extendedFloatingActionButton2.setOnClickListener {

            val data = PlaceOrder(listOf(productID!!),
                listOf(currentProductName!!),
                listOf(currentProductPrice!!),
                listOf(currentProductDetails!!),
                listOf(currentProductImage!!),
                listOf(binding.setQuantity.text.toString()),
                listOf(currentSellerId!!)
            )

            val intent = Intent(this , PaymentScreen::class.java)
            intent.putExtra("single_order_detail" , data)
            startActivity(intent)

        }
    }

}