package com.example.aritify.mvvm

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aritify.MyApplication
import com.example.aritify.Utils
import com.example.aritify.dataclasses.AllProductsData
import com.example.aritify.dataclasses.ArtifyUser
import com.example.aritify.dataclasses.OrderDetail
import com.example.aritify.dataclasses.PlaceOrder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel : ViewModel() {

    private lateinit var database : FirebaseDatabase
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    init {
        retrive_item_data()
        retrive_cart_data()
    }

    fun upload_user_data(user : ArtifyUser){
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firestore.collection("artify_user").document(auth.currentUser?.uid.toString()).set(user)
    }

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

    val myItem = MutableLiveData<AllProductsData>()
    fun retrive_item_data() =viewModelScope.launch (Dispatchers.IO){
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        try {
            firestore.collection("All_Products").document("All_Products")
                .addSnapshotListener{value , error->

                    if (error != null){
                        return@addSnapshotListener
                    }
                    if (value!!.exists()) {
                        val itemData = value.toObject(AllProductsData::class.java)
                        myItem.value = itemData!!
                    }
                }
        }catch (_: Exception){}
    }

    val myCatItem = MutableLiveData<AllProductsData>()
    fun retrive_item_data_category(collectionName : String) =viewModelScope.launch (Dispatchers.IO){
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        try {
            firestore.collection("Category").document(collectionName)
                .addSnapshotListener{value , error->

                    if (error != null){
                        return@addSnapshotListener
                    }
                    if (value!!.exists()) {
                        val itemData = value.toObject(AllProductsData::class.java)
                        myCatItem.value = itemData!!
                    }
                }
        }catch (_: Exception){}
    }

    val myCartItem = MutableLiveData<PlaceOrder>()
    fun retrive_cart_data() =viewModelScope.launch (Dispatchers.IO){
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        try {
            firestore.collection("Cart").document(auth.currentUser?.uid.toString())
                .addSnapshotListener{value , error->

                    if (error != null){
                        return@addSnapshotListener
                    }
                    if (value!!.exists()) {
                        val itemData = value.toObject(PlaceOrder::class.java)
                        myCartItem.value = itemData!!
                    }
                }
        }catch (_: Exception){}
    }

    fun retrive_user_data(callback: (ArtifyUser) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        firestore.collection("artify_user").document(auth.currentUser?.uid.toString()).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val value = documentSnapshot.toObject(ArtifyUser::class.java)
                callback(value!!)
            }
        }
    }



}