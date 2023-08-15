package com.example.aritify.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.example.aritify.R
import com.example.aritify.databinding.ActivityGetStartedBinding

class GetStarted : AppCompatActivity() {

    private lateinit var binding : ActivityGetStartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.transparent)

        binding.getStartedButton.setOnClickListener{
            val intent = Intent(this@GetStarted, LoginPage::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right)
//            finish()
        }
    }
}