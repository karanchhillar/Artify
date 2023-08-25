package com.example.aritify.dataclasses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceOrder(
    var product_id : MutableList<String> = mutableListOf(),
    var product_name :  MutableList<String> = mutableListOf(),
    var price : MutableList<Int> = mutableListOf(),
    var product_description : MutableList<String> = mutableListOf(),
    var product_image : MutableList<String> = mutableListOf(),
    var available_quantity : MutableList<String> = mutableListOf(),
    var seller_id : MutableList<String> = mutableListOf()
): Parcelable


