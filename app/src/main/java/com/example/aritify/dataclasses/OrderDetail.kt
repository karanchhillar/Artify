package com.example.aritify.dataclasses


data class OrderDetail(


    var rec_name: String = "",
    var address: String = "",
    var rec_phone : String = "",
    var product_id: List<Any> = emptyList(),
    var product_quantity: List<Any> = emptyList()


)


