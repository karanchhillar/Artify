package com.example.aritify.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.aritify.MainActivity
import com.example.aritify.R
import com.example.aritify.databinding.ActivityLoginPageBinding
import com.saadahmedsoft.shortintent.Anim
import com.saadahmedsoft.shortintent.ShortIntent

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

        binding.newHereRegisterText.setOnClickListener {
            val intent = Intent(this@LoginPage, RegisterPage::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right)
        }




        binding.loginButton.setOnClickListener {
            startActivity(Intent(this , MainActivity::class.java))
        }


//        binding.newHereRegisterText.setOnClickListener {
//            ShortIntent.getInstance(this).addDestination(RegisterPage::class.java)
//                .addTransition(Anim.SPIN).finish(this)
//
//        }


//        binding.newHereRegisterText.setOnClickListener {
//            val fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_right)
//            fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
//                override fun onAnimationStart(animation: Animation?) {}
//
//                override fun onAnimationEnd(animation: Animation?) {
//                    val intent = Intent(this@LoginPage, RegisterPage::class.java)
//                    startActivity(intent)
//                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
//                }
//
//                override fun onAnimationRepeat(animation: Animation?) {}
//            })

            // Start fade-out animation
//            binding.newHereRegisterText.startAnimation(fadeOutAnimation)
//        }
    }
}