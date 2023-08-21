package com.example.aritify.dataclasses

data class AllProductsData(
    var productId : List<String> = emptyList(),
    var productName :  List<String> = emptyList(),
    var price : List<Int> = emptyList(),
    var productDescription : List<String> = emptyList(),
    var productImage : List<String> = emptyList()
)
