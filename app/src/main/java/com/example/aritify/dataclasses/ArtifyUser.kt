package com.example.aritify.dataclasses

data class ArtifyUser(
    var name : String ?= null ,
    var phone_number : Long ?= null ,
    var DOB : String ?= null ,
    var address : String ?= null ,
    var profile_photo : String ?= null
)
