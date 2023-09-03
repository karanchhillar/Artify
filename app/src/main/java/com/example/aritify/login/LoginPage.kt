package com.example.aritify.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.aritify.MainActivity
import com.example.aritify.R
import com.example.aritify.UserInformation
import com.example.aritify.databinding.ActivityLoginPageBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


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
//            binding.loginPb.visibility = View.VISIBLE
            binding.pg1.visibility = View.VISIBLE

            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()

            if (TextUtils.isEmpty(emailInput) || TextUtils.isEmpty(passwordInput) ){
                Toast.makeText(this, "Cannot be Empty", Toast.LENGTH_SHORT).show()
                binding.pg1.visibility = View.GONE
                return@setOnClickListener
            }


            auth.signInWithEmailAndPassword(emailInput , passwordInput)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful){
                        val verification = auth.currentUser?.isEmailVerified
                        if (verification == true){
                            val intent = Intent(this, UserInformation::class.java)
                            startActivity(intent)
                            binding.pg1.visibility = View.GONE
                        }
                        else{
                            binding.pg1.visibility = View.GONE
                            Toast.makeText(this, "Please verify Your Email (check in spam)", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    else{
                        binding.pg1.visibility = View.GONE
                        Toast.makeText(this, "${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
//            binding.loginPb.visibility = View.INVISIBLE

        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this , gso)

        googleSignInClient.signOut()


        binding.imageViewGoogle.setOnClickListener{
            binding.pg1.visibility = View.VISIBLE
            signInGoogle()
        }


        binding.textView3.setOnClickListener {
            binding.pg1.visibility = View.VISIBLE

            if (binding.editTextEmail.text.toString().isEmpty()){
                Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show()
            }
            else{
                auth.sendPasswordResetEmail(binding.editTextEmail.text.toString()).addOnSuccessListener {
                    Toast.makeText(this, "Email has been sent to your email",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Some Exception occured",Toast.LENGTH_SHORT).show()
                }
            }
            binding.pg1.visibility = View.GONE

        }
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
                    binding.pg1.visibility = View.INVISIBLE
                    val intent : Intent = Intent(this , UserInformation::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this , it.exception.toString() , Toast.LENGTH_SHORT).show()
                }
            }
    }
}