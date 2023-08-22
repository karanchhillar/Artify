package com.example.aritify

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aritify.adapter.AllProductAdapter
import com.example.aritify.databinding.ActivityAllProductBinding
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


        binding.categoryTextView2.setText(category)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.transparent)

        vm = ViewModelProvider(this)[ViewModel::class.java]
        val allProductAdapter = AllProductAdapter()


        if (category == "All Products") {
            vm.myItem.observe(this, Observer {
                allProductAdapter.setItemList1(it)
                allProductAdapter.notifyDataSetChanged()
//            allProductAdapter.setItemList1(it)
                Toast.makeText(this, "${it.price[0]}", Toast.LENGTH_SHORT).show()
            })
        }else{
            vm.retrive_item_data_category(category!!)
            vm.myCatItem.observe(this, Observer {
                allProductAdapter.setItemList1(it)
                allProductAdapter.notifyDataSetChanged()
//            allProductAdapter.setItemList1(it)
                Toast.makeText(this, "${it.price[0]}", Toast.LENGTH_SHORT).show()
            })
        }

        binding.productRecylerview.adapter = allProductAdapter

    }
}