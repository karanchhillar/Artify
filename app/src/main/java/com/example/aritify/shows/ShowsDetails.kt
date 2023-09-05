package com.example.aritify.shows

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.example.aritify.R
import com.example.aritify.cart.PaymentActivity
import com.example.aritify.databinding.ActivityShowsDetailsBinding
import com.squareup.picasso.Picasso

class ShowsDetails : AppCompatActivity() {

    private lateinit var binding : ActivityShowsDetailsBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.transparent)

        val currentShowID : String? = intent.getStringExtra("currentShowID")
        val currentShowHostID : String? = intent.getStringExtra("currentShowHostID")
        val currentShowName : String? = intent.getStringExtra("currentShowName")
        val currentShowImage : String? = intent.getStringExtra("currentShowImage")
        val currentShowVenue: String? = intent.getStringExtra("currentShowVenue")
        val currentTicketPrice: Int = intent.getIntExtra("currentTicketPrice" , 0)
        val currentNoOfSeats: Int = intent.getIntExtra("currentNoOfSeats" , 0)
        val currentShowDate: String? = intent.getStringExtra("currentShowDate")
        val currentShowTiming: String? = intent.getStringExtra("currentShowTiming")
        val currentShowDuration: String? = intent.getStringExtra("currentShowDuration")
        val currentShowLanguage : String? = intent.getStringExtra("currentShowLanguage")
        val currentShowArtistName : String? = intent.getStringExtra("currentShowArtistName")
        val currentShowCategory : String? = intent.getStringExtra("currentShowCategory")
        val currentShowDescription : String? = intent.getStringExtra("currentShowDescription")


        Picasso.get().load(currentShowImage).into(binding.imgShow)
        binding.showCatType.text = currentShowCategory.toString().uppercase() + " SHOWS"
        binding.showTvproductname1.text = currentShowName.toString()
        binding.showTvproductdesc.text = currentShowDescription.toString()
        binding.showCategory.text = currentShowCategory.toString()
        binding.showDuration.text = currentShowDuration.toString()
        binding.showLanguage.text = currentShowLanguage.toString()
        binding.showArtistName.text = currentShowArtistName.toString()
        binding.showDateAndTime.text = currentShowDate.toString() + " , " + currentShowTiming.toString()
        binding.showVenue1.text = currentShowVenue.toString()
        binding.showTvPrice.text = "₹ $currentTicketPrice"

        binding.showImgplusicon.setOnClickListener {

            val curr_val = binding.showSetQuantity.text.toString().toInt()
            if (curr_val == currentNoOfSeats.toString().toInt()){
                Toast.makeText(this, "Can't Book Tickets More", Toast.LENGTH_SHORT).show()
            }
            else{
                binding.showSetQuantity.text = (curr_val+1).toString()
            }
        }
        binding.showImgminusicon.setOnClickListener {
            val curr_val = binding.showSetQuantity.text.toString().toInt()
            if (curr_val == 1){
                Toast.makeText(this, "Can't Be Less Then 0", Toast.LENGTH_SHORT).show()
            }
            else{
                binding.showSetQuantity.text = (curr_val-1).toString()
            }
        }

        binding.showsProductDetailsBackButton.setOnClickListener {
            finish()
        }

        binding.showBookNow.setOnClickListener {
            val intent = Intent(this , PaymentActivity::class.java)
            intent.putExtra("method" , "show_ticket")
//            intent.putExtra("order_details" , orderDetails)
            startActivity(intent)
        }

    }
}