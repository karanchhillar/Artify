package com.example.aritify.cart

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.example.aritify.R
import com.example.aritify.dataclasses.OrderDetail
import com.example.aritify.mvvm.ViewModel
import com.example.aritify.order.OrderPlaced
import com.razorpay.Checkout
import com.razorpay.ExternalWalletListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject
import java.security.acl.Owner

class PaymentActivity: AppCompatActivity(), PaymentResultWithDataListener, ExternalWalletListener, DialogInterface.OnClickListener {

    val TAG:String = PaymentActivity::class.toString()
    private lateinit var alertDialogBuilder: AlertDialog.Builder

    private lateinit var orderDetail: OrderDetail
    private lateinit var vm : ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.transparent)

        vm = ViewModelProvider(this@PaymentActivity)[ViewModel::class.java]
        /*
        * To ensure faster loading of the Checkout form,
        * call this method as early as possible in your checkout flow
        * */
        Checkout.preload(applicationContext)
        alertDialogBuilder = AlertDialog.Builder(this@PaymentActivity)
        alertDialogBuilder.setTitle("Payment Result")
        alertDialogBuilder.setCancelable(true)
        alertDialogBuilder.setPositiveButton("Ok",this)

        val method = intent.getStringExtra("method")

        val button: Button = findViewById(R.id.btn_pay)
        button.setOnClickListener {
            if (method == "online"){
                startPayment()
            }
            else{
                val intent = Intent(this , OrderPlaced::class.java)
                startActivity(intent)
            }

        }
    }

    private fun startPayment() {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val activity:Activity = this
        val co = Checkout()
        val etApiKey = findViewById<EditText>(R.id.et_api_key)
        val etCustomOptions = findViewById<EditText>(R.id.et_custom_options)

        if (!TextUtils.isEmpty(etApiKey.text.toString())){
            co.setKeyID(etApiKey.text.toString())
        }
        try {
            var options = JSONObject()
            if (!TextUtils.isEmpty(etCustomOptions.text.toString())){
                options = JSONObject(etCustomOptions.text.toString())
            }else{
                options.put("name","Artify")
                options.put("description","Made With ❤️")
                //You can omit the image option to fetch the image from dashboard
                options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
                options.put("currency","INR")
                options.put("amount","100")
                options.put("send_sms_hash",true);

                val prefill = JSONObject()
//                prefill.put("email","test@razorpay.com")
//                prefill.put("contact","9021066696")

                prefill.put("email","chhillarkaran2310@gmail.com")
                prefill.put("contact","9306853815")

                options.put("prefill",prefill)
            }

            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        try{
            alertDialogBuilder.setMessage("Payment Successful : Payment ID: $p0\nPayment Data: ${p1?.data}")
            alertDialogBuilder.show()
        }catch (e: Exception){
            e.printStackTrace()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            orderDetail = intent.extras!!.getParcelable<OrderDetail>("order_details")!!
        } else {
            // Use something else that could work if there's something
            Toast.makeText(this@PaymentActivity, "Version is Low", Toast.LENGTH_SHORT).show()
        }

        vm.placeOrder(orderDetail)

        val intent = Intent(this , OrderPlaced::class.java)
        intent.putExtra("paid_amt" , orderDetail.price.toString())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //clear call stack
        startActivity(intent)
        finish()
    }


    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        try {
            alertDialogBuilder.setMessage("Payment Failed : Payment Data: ${p2?.data}")
            alertDialogBuilder.show()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onExternalWalletSelected(p0: String?, p1: PaymentData?) {
        try{
            alertDialogBuilder.setMessage("External wallet was selected : Payment Data: ${p1?.data}")
            alertDialogBuilder.show()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
    }
}