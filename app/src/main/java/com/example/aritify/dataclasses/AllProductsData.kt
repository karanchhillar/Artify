package com.example.aritify.dataclasses

data class AllProductsData(
    var product_id : List<String> = emptyList(),
    var product_name :  List<String> = emptyList(),
    var price : List<Int> = emptyList(),
    var product_description : List<String> = emptyList(),
    var product_image : List<String> = emptyList()
)
