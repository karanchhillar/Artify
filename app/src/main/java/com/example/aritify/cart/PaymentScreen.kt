package com.example.aritify.cart

//import com.example.aritify.DataBinderMapperImpl
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aritify.R
import com.example.aritify.adapter.CartAdapter
import com.example.aritify.adapter.PlaceOrderAdapter
import com.example.aritify.databinding.ActivityPaymentScreenBinding
import com.example.aritify.dataclasses.OrderDetail
import com.example.aritify.dataclasses.PlaceOrder
import com.example.aritify.mvvm.ViewModel
import com.squareup.picasso.Picasso

class PaymentScreen : AppCompatActivity() {

    private lateinit var binding : ActivityPaymentScreenBinding
    private lateinit var single_data : PlaceOrder
    private lateinit var vm : ViewModel


    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.transparent)

        binding.paymentScreenBackButton.setOnClickListener {
            finish()
        }

        val orderDetails = OrderDetail()

        vm = ViewModelProvider(this).get(ViewModel::class.java)

        vm.retrive_user_data {
            binding.name3.setText(it.name)
            binding.paymentPhoneText.setText(it.phone_number.toString())
            binding.address.setText(it.address)
        }

        val myCart : String? = intent.getStringExtra("my_cart")
        vm = ViewModelProvider(this)[ViewModel::class.java]

        if (myCart == "1"){
            val adapter = PlaceOrderAdapter()

            vm.myCartItem.observe(this, Observer {

                orderDetails.product_id = it.product_id
                orderDetails.product_quantity = it.available_quantity
                orderDetails.seller_id = it.seller_id

                adapter.setItemList1(it)
                adapter.notifyDataSetChanged()
            })
            binding.yourOrderRecyclerView.layoutManager = LinearLayoutManager(this)

            binding.yourOrderRecyclerView.adapter = adapter

            adapter.itemTotal_price.observe(this , Observer{

                orderDetails.price = it

                binding.totalPrice.text = "₹ $it"
                binding.grandTotolPrice.text = "₹ $it"
            })

        }
        else {
            val adapter = PlaceOrderAdapter()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                single_data = intent.extras!!.getParcelable("single_order_detail")!!
            } else {
                // Use something else that could work if there's something
                Toast.makeText(this@PaymentScreen, "Version is Low", Toast.LENGTH_SHORT).show()
            }

//        val single_data = intent.getParcelableExtra("single_order_detail", PlaceOrder::class.java)
            adapter.setItemList1(single_data)

            orderDetails.product_id = single_data.product_id
            orderDetails.product_quantity = single_data.available_quantity
            orderDetails.seller_id = single_data.seller_id

            binding.yourOrderRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.yourOrderRecyclerView.adapter = adapter

            adapter.itemTotal_price.observe(this , Observer{
                orderDetails.price = it

                binding.totalPrice.text = "₹ $it"
                binding.grandTotolPrice.text = "₹ $it"
            })
        }

        binding.paymentPayNow.setOnClickListener {
            val nameInput = binding.name3.text.toString()
            val phoneInput = binding.paymentPhoneText.text.toString()
            val addressInput = binding.address.text.toString()

            if (TextUtils.isEmpty(nameInput) || TextUtils.isEmpty(phoneInput) || TextUtils.isEmpty(addressInput)) {
                Toast.makeText(this, "Cannot be Empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                orderDetails.rec_name = nameInput
                orderDetails.rec_phone = phoneInput
                orderDetails.address = addressInput
            }

            if(binding.radioButton1.isChecked){
                val intent = Intent(this , PaymentActivity::class.java)
                intent.putExtra("method" , "online")
                intent.putExtra("order_details" , orderDetails)
                startActivity(intent)
            }
            else{
                val intent = Intent(this , PaymentActivity::class.java)
                intent.putExtra("method" , "offline")
                intent.putExtra("order_details" , orderDetails)
                startActivity(intent)
            }

        }


    }
}