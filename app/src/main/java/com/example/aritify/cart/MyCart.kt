package com.example.aritify.cart

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.aritify.R
import com.example.aritify.adapter.CartAdapter
import com.example.aritify.databinding.ActivityMyCartBinding
import com.example.aritify.mvvm.ViewModel

class MyCart : AppCompatActivity() {

    private lateinit var binding: ActivityMyCartBinding
    private lateinit var vm : ViewModel

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.transparent)

        binding.placeOrderButton.setOnClickListener{
            val intent = Intent(this , PaymentScreen::class.java)
            intent.putExtra("my_cart" , "1")
            startActivity(intent)
        }

        vm = ViewModelProvider(this)[ViewModel::class.java]

        val adapter = CartAdapter()

        vm.myCartItem.observe(this, Observer {
            binding.totalQuantityTv2.text = "${it.price.size} items"
            adapter.setItemList2(it)
            adapter.notifyDataSetChanged()
        })

        adapter.itemTotal_price.observe(this , Observer{
            binding.totalPriceTv.text = "Total: ₹${it}"
        })

        binding.recyclerView.adapter = adapter

    }
}