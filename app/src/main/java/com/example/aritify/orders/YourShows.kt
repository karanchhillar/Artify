
package com.example.aritify.orders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.aritify.MainActivity
import com.example.aritify.R
import com.example.aritify.databinding.ActivityYourOrdersBinding
import com.example.aritify.databinding.ActivityYourShowsBinding
import com.example.aritify.fragment.HomeFragment
import com.example.aritify.fragment.MicFragment

class YourShows : AppCompatActivity() {
    private lateinit var binding : ActivityYourShowsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYourShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.transparent)

        binding.btnContinueShopping1.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}