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