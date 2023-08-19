package com.example.aritify.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.aritify.AllProduct
import com.example.aritify.ProductDetails
import com.example.aritify.R
import com.example.aritify.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

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


        binding.allProductViewAll.setOnClickListener {
            startActivity(Intent(requireContext() , AllProduct::class.java))
        }
        binding.searchButton.setOnClickListener {
            startActivity(Intent(requireContext() , ProductDetails::class.java))
        }
    }
}