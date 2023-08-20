package com.example.aritify.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.aritify.MainActivity
import com.example.aritify.R
import com.example.aritify.databinding.ActivityLoginPageBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.saadahmedsoft.shortintent.Anim
import com.saadahmedsoft.shortintent.ShortIntent

class LoginPage : AppCompatActivity() {

    private lateinit var binding : ActivityLoginPageBinding

    private lateinit var auth: FirebaseAuth
    private  lateinit var googleSignInClient: GoogleSignInClient

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

        val email = binding.editTextEmail
        val password = binding.editTextPassword

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.signupButton.setOnClickListener {

            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()

            if (TextUtils.isEmpty(emailInput) || TextUtils.isEmpty(passwordInput) ){
                Toast.makeText(this, "Cannot be Empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            auth.signInWithEmailAndPassword(emailInput , passwordInput)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful){
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(this, "${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this , gso)

        googleSignInClient.signOut()


        binding.imageViewGoogle.setOnClickListener{
            signInGoogle()
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

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }
    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }else{
                Toast.makeText(this , task.exception.toString() , Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken , null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    val intent : Intent = Intent(this , MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this , it.exception.toString() , Toast.LENGTH_SHORT).show()
                }
            }
    }
}