package com.example.aritify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.aritify.adapter.CartAdapter
import com.example.aritify.cart.MyCart
import com.example.aritify.databinding.ActivityMainBinding
import com.example.aritify.fragment.HomeFragment
import com.example.aritify.fragment.MicFragment
import com.example.aritify.mvvm.ViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var vm : ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // smaple

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.transparent)

        val navView = binding.navBar
        navView.itemIconTintList = null;
        val homefragment = HomeFragment()
        val micfragment = MicFragment()

        vm = ViewModelProvider(this)[ViewModel::class.java]




        setFragment(homefragment)

//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        window.navigationBarColor = getColor(R.color.primaryColor)

        navView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    setFragment(homefragment)
                }
                R.id.mic -> {
                    setFragment(micfragment)
                }
                R.id.cart -> {
                    startActivity(Intent(this , MyCart::class.java))
                }
            }
            true
        }
        vm.myCartItem.observe(this, Observer {
            navView.getOrCreateBadge(R.id.cart).number =  it.price.size
        })

    }
    private fun setFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction().apply {

            replace(R.id.frame_id,fragment)
            commit()

        }

    }


}