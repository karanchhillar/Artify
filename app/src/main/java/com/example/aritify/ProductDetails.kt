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
import com.example.aritify.databinding.ProductsItemListBinding
import com.example.aritify.dataclasses.PlaceOrder
import com.example.aritify.mvvm.ViewModel
import com.google.firebase.database.PropertyName
import com.squareup.picasso.Picasso

class ProductDetails : AppCompatActivity() {

    private lateinit var binding : ActivityProductDetailsBinding
    private lateinit var vm : ViewModel
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        vm = ViewModelProvider(this,viewModelFactory)[ViewModel::class.java]



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

            var curr_val = binding.setQuantity.text.toString().toInt()
            Toast.makeText(this, "${currentProductQuantity!!.toInt()}", Toast.LENGTH_SHORT).show()
            if (curr_val == currentProductQuantity.toInt()){
                Toast.makeText(this, "Can't Buy More", Toast.LENGTH_SHORT).show()
            }
            else{
                binding.setQuantity.text = (curr_val+1).toString()
            }
        }
        binding.imgminusicon.setOnClickListener {
            var curr_val = binding.setQuantity.text.toString().toInt()
            if (curr_val == 1){
                Toast.makeText(this, "Can't Be Less Then 0", Toast.LENGTH_SHORT).show()
            }
            else{
                binding.setQuantity.text = (curr_val-1).toString()
            }
        }

        binding.efaAddToCart.setOnClickListener{

//            Toast.makeText(this, "${binding.efaAddToCart.text}", Toast.LENGTH_SHORT).show()
            if (binding.efaAddToCart.text == "Add To Cart"){
                vm.my_cart.product_id.add(productID!!)
                vm.my_cart.product_name.add(currentProductName!!)
                vm.my_cart.price.add(currentProductPrice!!)
                vm.my_cart.product_description.add(currentProductDetails!!)
                vm.my_cart.product_image.add(currentProductImage!!)
                vm.my_cart.available_quantity.add(binding.setQuantity.text.toString()!!)
                vm.my_cart.seller_id.add(currentSellerId!!)
                binding.efaAddToCart.text = "Remove From Cart"
                Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
            }
            else{
                vm.my_cart.product_id.removeLast()
                vm.my_cart.product_name.removeLast()
                vm.my_cart.price.removeLast()
                vm.my_cart.product_description.removeLast()
                vm.my_cart.product_image.removeLast()
                vm.my_cart.available_quantity.removeLast()
                vm.my_cart.seller_id.removeLast()
                binding.efaAddToCart.text = "Add To Cart"
                Toast.makeText(this, "Removed To Cart", Toast.LENGTH_SHORT).show()
            }

//


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