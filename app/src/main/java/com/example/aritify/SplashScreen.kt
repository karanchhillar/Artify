package com.example.aritify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.aritify.login.GetStarted
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {

    private val splashTimeOut: Long = 2000 // Splash screen duration in milliseconds
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = FirebaseAuth.getInstance()

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.transparent)

        Handler().postDelayed({
            // Start the main activity after the splash screen duration
            if (auth.currentUser == null){
                val intent = Intent(this, GetStarted::class.java)
                startActivity(intent)
                finish() // Finish the splash activity
            }
            else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, splashTimeOut)

    }
}