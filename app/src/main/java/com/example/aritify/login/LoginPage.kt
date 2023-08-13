package com.example.aritify.login

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.aritify.R
import com.example.aritify.databinding.ActivityLoginPageBinding

class LoginPage : AppCompatActivity() {

    private lateinit var binding : ActivityLoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.transparent)

//        binding.newHereRegisterText.setOnClickListener {
//            startActivity(Intent(this , RegisterPage::class.java))
//        }

        binding.newHereRegisterText.setOnClickListener {
            val fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    val intent = Intent(this@LoginPage, RegisterPage::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })

            // Start fade-out animation
            binding.newHereRegisterText.startAnimation(fadeOutAnimation)
        }
    }
}