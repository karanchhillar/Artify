package com.example.aritify.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.PagerAdapter
import com.example.aritify.R
import com.example.aritify.dataclasses.ShowsData
import com.example.aritify.shows.ShowsDetails
import com.squareup.picasso.Picasso
import java.util.Objects

class ViewPagerAdapter(val context: Context) : PagerAdapter(){

    var itemList = ShowsData()
    fun setItemList1(item : ShowsData){
        itemList = item
    }
    override fun getCount(): Int {
        return itemList.ticket_price.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    // on below line we are initializing
    // our item and inflating our layout file
    @SuppressLint("SetTextI18n")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val n = position
        // on below line we are initializing
        // our layout inflater.
        val mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // on below line we are inflating our custom
        // layout file which we have created.
        val itemView: View = mLayoutInflater.inflate(R.layout.talent_item_list, container, false)

        // on below line we are initializing
        // our image view with the id.
        val imageView: ImageView = itemView.findViewById<View>(R.id.show_image) as ImageView
        val title : TextView = itemView.findViewById(R.id.title)
        val category : TextView = itemView.findViewById(R.id.category)
        val showDescription : TextView = itemView.findViewById(R.id.show_description)
        val price : TextView = itemView.findViewById(R.id.price)
        val bookNowButton : Button = itemView.findViewById(R.id.book_now_button)

        // on below line we are setting
        // image resource for image view.
        Picasso.get().load(itemList.show_image[position]).into(imageView)
        title.text = itemList.show_name[position]
        category.text = itemList.show_category[position]
        price.text = "₹ ${itemList.ticket_price[position]} onwards"
        showDescription.text = itemList.show_description[position]

        val currentShowID : String = itemList.show_id[n]
        val currentShowHostID : String = itemList.show_host_id[n]
        val currentShowName : String = itemList.show_name[n]
        val currentShowImage : String = itemList.show_image[n]
        val currentShowVenue : String = itemList.show_venue[n]
        val currentShowDate : String = itemList.show_date[n]
        val currentShowTiming : String = itemList.show_timing[n]
        val currentShowDuration : String = itemList.show_duration[n]
        val currentShowLanguage : String = itemList.show_language[n]
        val currentShowArtistName : String = itemList.artist_name[n]
        val currentShowCategory : String = itemList.show_category[n]
        val currentShowDescription: String = itemList.show_description[n]
        val currentTicketPrice: Int = itemList.ticket_price[n]
        val currentNoOfSeats: Int = itemList.no_of_seats[n]
        bookNowButton.setOnClickListener {
            val intent = Intent(context , ShowsDetails::class.java)
            intent.putExtra("currentShowID" , currentShowID)
            intent.putExtra("currentShowHostID" , currentShowHostID)
            intent.putExtra("currentShowName" , currentShowName)
            intent.putExtra("currentShowImage" , currentShowImage)
            intent.putExtra("currentShowVenue" , currentShowVenue)
            intent.putExtra("currentShowDate" , currentShowDate)
            intent.putExtra("currentShowTiming" , currentShowTiming)
            intent.putExtra("currentShowDuration" ,currentShowDuration)
            intent.putExtra("currentShowLanguage" , currentShowLanguage)
            intent.putExtra("currentShowArtistName" , currentShowArtistName)
            intent.putExtra("currentShowCategory" , currentShowCategory)
            intent.putExtra("currentShowDescription" , currentShowDescription)
            intent.putExtra("currentTicketPrice" , currentTicketPrice)
            intent.putExtra("currentNoOfSeats" , currentNoOfSeats)

            context.startActivity(intent)
        }
//        imageView.setImageResource(itemList.ticket_price.get(position))

        // on the below line we are adding this
        // item view to the container.
        Objects.requireNonNull(container).addView(itemView)

        // on below line we are simply
        // returning our item view.
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // on below line we are removing view
        container.removeView(`object` as ConstraintLayout)
    }
}