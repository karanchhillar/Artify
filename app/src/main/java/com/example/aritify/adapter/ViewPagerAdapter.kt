package com.example.aritify.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.aritify.R
import java.util.Objects

class ViewPagerAdapter(val context: Context, val imageList: List<Int>) : PagerAdapter() {

    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    // on below line we are initializing
    // our item and inflating our layout file
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // on below line we are initializing
        // our layout inflater.
        val mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // on below line we are inflating our custom
        // layout file which we have created.
        val itemView: View = mLayoutInflater.inflate(R.layout.talent_item_list, container, false)

        // on below line we are initializing
        // our image view with the id.
        val imageView: ImageView = itemView.findViewById<View>(R.id.show_image) as ImageView

        // on below line we are setting
        // image resource for image view.
        imageView.setImageResource(imageList.get(position))

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