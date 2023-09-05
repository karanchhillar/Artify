package com.example.aritify.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aritify.ProductDetails
import com.example.aritify.databinding.ShowsItemListBinding
import com.example.aritify.dataclasses.AllProductsData
import com.example.aritify.dataclasses.ShowsData
import com.squareup.picasso.Picasso

class ShowsAdapter  : RecyclerView.Adapter<ShowViewHolder>()  {

    var itemList = ShowsData()
    private lateinit var binding: ShowsItemListBinding

    fun setItemList1(item : ShowsData){
        itemList = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        binding = ShowsItemListBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return  ShowViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.ticket_price.size
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        val n = itemList.ticket_price.size - position - 1

//        val position = getAdapterPosition();
        if(n != RecyclerView.NO_POSITION){
            // you can trust the adapter position
            // do whatever you intend to do with this position

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

            holder.showName.text = currentShowName
            holder.showVenue.text = currentShowVenue
            holder.show_date.text = currentShowDate
            holder.ticketPrice.text = "₹ $currentTicketPrice onwards"
            Picasso.get().load(currentShowImage).into(holder.showImage)

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context , ProductDetails::class.java)
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

                holder.itemView.context.startActivity(intent)
            }
        }
    }
}

class ShowViewHolder(binding: ShowsItemListBinding) : RecyclerView.ViewHolder(binding.root){
    val showImage = binding.showImage
    val showName = binding.showName
    val showVenue = binding.showName
    val ticketPrice = binding.ticketPrice
    val show_date = binding.showDate
}