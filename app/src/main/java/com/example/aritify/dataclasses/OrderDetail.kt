package com.example.aritify.dataclasses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class OrderDetail(
    var rec_name: String = "",
    var address: String = "",
    var rec_phone : String = "",
    var product_id: List<String> = emptyList(),
    var price: Int = 0,
    var product_quantity: List<String> = emptyList(),
    var seller_id : List<String> = emptyList()
): Parcelable
