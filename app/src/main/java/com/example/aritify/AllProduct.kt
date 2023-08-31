package com.example.aritify

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aritify.adapter.AllProductAdapter
import com.example.aritify.databinding.ActivityAllProductBinding
import com.example.aritify.dataclasses.AllProductsData
import com.example.aritify.mvvm.ViewModel

class AllProduct : AppCompatActivity() {

    private lateinit var binding: ActivityAllProductBinding
    private lateinit var vm : ViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val category = intent.getStringExtra("Category")
        val searchText : String? = intent.getStringExtra("search_text")

        binding.categoryTextView2.setText(category)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.transparent)

        binding.allProductBackButton.setOnClickListener {
            finish()
        }

        vm = ViewModelProvider(this@AllProduct)[ViewModel::class.java]
        val allProductAdapter = AllProductAdapter()

        var size = 1

        if (category == "All Products") {
            vm.myItem.observe(this, Observer {
                size = it.price.size
                if (size != 0) binding.noProdImg.isVisible = false
                allProductAdapter.setItemList1(it)
                allProductAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Total item:${it.price.size}", Toast.LENGTH_SHORT).show()
            })
        }
        else if(category == "Results"){
            vm.myItem.observe(this, Observer {
                var it2 = AllProductsData()
                val product_id_array : MutableList<String> = mutableListOf()
                val product_name_array :  MutableList<String> = mutableListOf()
                val price_array : MutableList<Int> = mutableListOf()
                val product_description_array : MutableList<String> = mutableListOf()
                val product_image_array : MutableList<String> = mutableListOf()
                val available_quantity_array : MutableList<String> = mutableListOf()
                val seller_id_array : MutableList<String> = mutableListOf()

                for (i in it.product_name){
                    if(searchText.toString().lowercase() in i.lowercase()){
                        val index = it.product_name.indexOf(i)

                        product_id_array.add(it.product_id[index])
                        product_name_array.add(it.product_name[index])
                        price_array.add(it.price[index])
                        product_description_array.add(it.product_description[index])
                        product_image_array.add(it.product_image[index])
                        available_quantity_array.add(it.available_quantity[index])
                        seller_id_array.add(it.seller_id[index])
                    }
                }
                it2 = AllProductsData(
                    product_id = product_id_array,
                    product_name = product_name_array,
                    price = price_array,
                    product_description = product_description_array,
                    product_image = product_image_array,
                    available_quantity = available_quantity_array,
                    seller_id = seller_id_array
                )

                size = it2.price.size
                if (size != 0) binding.noProdImg.isVisible = false

                allProductAdapter.setItemList1(it2)
                allProductAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Total item:${it2.price.size}", Toast.LENGTH_SHORT).show()
            })
        }
        else{
            vm.retrive_item_data_category(category!!)
            vm.myCatItem.observe(this, Observer {
                size = it.price.size
                if (size != 0) binding.noProdImg.isVisible = false
                allProductAdapter.setItemList1(it)
                allProductAdapter.notifyDataSetChanged()

                Toast.makeText(this, "Total item:${it.price.size}", Toast.LENGTH_SHORT).show()
            })
        }

        binding.productRecylerview.adapter = allProductAdapter

    }
}