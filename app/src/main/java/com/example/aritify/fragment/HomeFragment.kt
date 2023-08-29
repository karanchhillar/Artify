package com.example.aritify.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.example.aritify.AllProduct
import com.example.aritify.ProductDetails
import com.example.aritify.R
import com.example.aritify.UserInformation
import com.example.aritify.databinding.FragmentHomeBinding
import com.example.aritify.mvvm.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth
    private lateinit var vm : ViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = FirebaseAuth.getInstance()

        vm = ViewModelProvider(this).get(ViewModel::class.java)

        vm.retrive_user_data {
            Picasso.get().load(it.profile_photo).into(binding.profilePhoto)
            binding.address.text = it.address
        }
        binding.profilePhoto.setOnClickListener{
            startActivity(Intent(activity , UserInformation::class.java))
        }
        binding.llmen.setOnClickListener {
            showAllProductCategory("Mens")
        }
        binding.llwomen.setOnClickListener {
            showAllProductCategory("Womens")
        }
        binding.llhomedecore.setOnClickListener {
            showAllProductCategory("Home Decor")
        }
        binding.llpottery.setOnClickListener {
            showAllProductCategory("Pottery")
        }
        binding.llpainting.setOnClickListener {
            showAllProductCategory("Painting")
        }
        binding.lltoys.setOnClickListener {
            showAllProductCategory("Toys")
        }
        binding.llbags.setOnClickListener {
            showAllProductCategory("Bags")
        }
        binding.llother.setOnClickListener {
            showAllProductCategory("Others")
        }

        binding.allProductViewAll.setOnClickListener {
            showAllProductCategory("All Products")
        }
        binding.searchButton.setOnClickListener {
            startActivity(Intent(requireContext() , ProductDetails::class.java))
        }
    }

    private fun showAllProductCategory(s: String) {
        val intent = Intent(requireContext(),AllProduct::class.java)
        intent.putExtra("Category" , s)
        startActivity(intent)
    }
}