package com.example.aritify.cart

//import com.example.aritify.DataBinderMapperImpl
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aritify.R
import com.example.aritify.adapter.PlaceOrderAdapter
import com.example.aritify.databinding.ActivityPaymentScreenBinding
import com.example.aritify.dataclasses.PlaceOrder

class PaymentScreen : AppCompatActivity() {

    private lateinit var binding : ActivityPaymentScreenBinding
    private lateinit var single_data : PlaceOrder

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.transparent)

        val adapter = PlaceOrderAdapter()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Safe to use getMccString
            single_data= intent.extras!!.getParcelable("single_order_detail")!!

//            Toast.makeText(this@PaymentScreen, "${single_data.price[0]}", Toast.LENGTH_SHORT).show()
//            single_data = intent.getParcelableExtra("single_order_detail", PlaceOrder::class.java)!!

        } else {
            // Use something else that could work if there's something
            Toast.makeText(this@PaymentScreen, "Version is Low", Toast.LENGTH_SHORT).show()
        }

//        val single_data = intent.getParcelableExtra("single_order_detail", PlaceOrder::class.java)
        adapter.setItemList1(single_data!!)

        binding.yourOrderRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.yourOrderRecyclerView.adapter = adapter

    }
}