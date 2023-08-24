package com.example.aritify.dataclasses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceOrder(
    var product_id : List<String> = emptyList(),
    var product_name :  List<String> = emptyList(),
    var price : List<Int> = emptyList(),
    var product_description : List<String> = emptyList(),
    var product_image : List<String> = emptyList(),
    var available_quantity : List<String> = emptyList(),
    var seller_id : List<String> = emptyList()
): Parcelable

