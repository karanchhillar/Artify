package com.example.aritify.mvvm

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.aritify.MyApplication
import com.example.aritify.Utils
import com.example.aritify.dataclasses.OrderDetail
import com.google.firebase.database.FirebaseDatabase

class ViewModel : ViewModel() {

    private lateinit var database : FirebaseDatabase


    fun placeOrder(order : OrderDetail) {
        database = FirebaseDatabase.getInstance()
        database.reference.child("ORDER DEATILS").child(Utils.getUidLoggedIn())
            .child(Utils.getTime().toString()).setValue(order)
            .addOnSuccessListener {
                Toast.makeText(MyApplication.getAppContext(), "YOUR ORDER IS CONFIRMED \n ", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(MyApplication.getAppContext(), "FACING PROBLEM RIGHT NOW", Toast.LENGTH_SHORT).show()
            }
    }







}