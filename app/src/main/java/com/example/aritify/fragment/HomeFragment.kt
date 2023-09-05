package com.example.aritify.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.aritify.AllProduct
import com.example.aritify.R
import com.example.aritify.UserInformation
import com.example.aritify.adapter.AllProductAdapter
import com.example.aritify.cart.MyCart
import com.example.aritify.databinding.FragmentHomeBinding
import com.example.aritify.mvvm.ViewModel
import com.example.aritify.orders.YourOrders
import com.example.aritify.orders.YourShows
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth
    private lateinit var vm : ViewModel

    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        return binding.root

    }
    @SuppressLint("NotifyDataSetChanged", "RtlHardcoded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = FirebaseAuth.getInstance()

        vm = ViewModelProvider(this).get(ViewModel::class.java)

        vm.retrive_user_data {
            Picasso.get().load(it.profile_photo).into(binding.profilePhoto)
            binding.address.text = it.address
        }

        drawerLayout = binding.myDrawerLayout
        val actionBarDrawerToggle = ActionBarDrawerToggle(requireActivity(), drawerLayout, com.example.aritify.R.string.nav_open, com.example.aritify.R.string.nav_close)


        val menuIcon = binding.menuBars
        menuIcon.setOnClickListener { drawerLayout.openDrawer(Gravity.LEFT) }
        val navigationView: NavigationView = binding.navDraw
        navigationView.itemIconTintList = null

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.your_profile -> {
                    startActivity(Intent(requireContext() , UserInformation::class.java))
                }
                R.id.your_cart -> {
                    startActivity(Intent(requireContext() , MyCart::class.java))
                }
                R.id.your_orders -> {
                    startActivity(Intent(requireContext() , YourOrders::class.java))
                }
                R.id.your_shows -> {
                    startActivity(Intent(requireContext() , YourShows::class.java))
                }
            }
            true
        }

//        navigationView.setNavigationItemSelectedListener { menuItem ->
//            // Handle menu item selected
//            menuItem.isChecked = true
//            drawerLayout.close()
//            true
//        }

//        navigationView.setNavigationItemSelectedListener(requireActivity() as NavigationView.OnNavigationItemSelectedListener)
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
//        drawerLayout.addDrawerListener(actionBarDrawerToggle)
//        actionBarDrawerToggle.syncState()

        // to make the Navigation drawer icon always appear on the action bar   yes
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val Best4Products = AllProductAdapter()
        vm.myHomeItem.observe(requireActivity(), Observer {

            Best4Products.setItemList1(it)
            Best4Products.notifyDataSetChanged()
            Toast.makeText(requireContext(), "Total item:${it.price.size}", Toast.LENGTH_SHORT).show()
        })

        binding.paidPromoRecyclerView.adapter = Best4Products


        var allProductArray: List<String>

        vm.myItem.observe(requireActivity(), Observer {
            allProductArray = it.product_name
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, allProductArray)
            val textView = binding.autocompleteSearch as AutoCompleteTextView
            textView.setAdapter(adapter)
        })

        binding.searchButton.setOnClickListener {
            intentValues( "Results" , binding.autocompleteSearch.text.toString())
        }

        binding.autocompleteSearch.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    intentValues( "Results" , binding.autocompleteSearch.text.toString())
                    true
                }
                else -> false
            }
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

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun intentValues(s: String, valueOfSearch: String) {
        val intent = Intent(requireContext(),AllProduct::class.java)
        intent.putExtra("Category" , s)
        intent.putExtra("search_text" , valueOfSearch)
        startActivity(intent)
    }

    private fun showAllProductCategory(s: String) {
        val intent = Intent(requireContext(),AllProduct::class.java)
        intent.putExtra("Category" , s)
        startActivity(intent)
    }
}