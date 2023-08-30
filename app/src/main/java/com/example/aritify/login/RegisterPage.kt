package com.example.aritify.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ItemTouchHelper.ViewDropHandler
import com.example.aritify.MainActivity
import com.example.aritify.R
import com.example.aritify.UserInformation
import com.example.aritify.databinding.ActivityRegisterPageBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class RegisterPage : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterPageBinding

    private lateinit var auth: FirebaseAuth
    private  lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.transparent)

        binding.alreadyAMemText.setOnClickListener {
            val intent = Intent(this@RegisterPage, LoginPage::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val email : EditText = binding.editTextEmail1
        val password : EditText = binding.editTextPassword1
        val confirmPassword : EditText = binding.editTextConfirmPassword

        auth = FirebaseAuth.getInstance()

        binding.signupButton.setOnClickListener {
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()
            val confirmPasswordInput = confirmPassword.text.toString()

            if (TextUtils.isEmpty(emailInput) || TextUtils.isEmpty(passwordInput) || TextUtils.isEmpty(confirmPasswordInput)) {
                Toast.makeText(this, "Cannot be Empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (passwordInput.length < 6) {
                Toast.makeText(this, "Password should be more than 6 letters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (passwordInput != confirmPasswordInput) {
                Toast.makeText(this, "Both are not same", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.pg2.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(emailInput , passwordInput)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful){
                        Toast.makeText(this, "New Id created", Toast.LENGTH_SHORT).show()

                        auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                            binding.pg2.visibility = View.GONE
                            Toast.makeText(this, "Email has been sent to your mail", Toast.LENGTH_SHORT)
                                .show()
                        }
                            ?.addOnFailureListener {
                                binding.pg2.visibility = View.GONE
                                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        auth.signOut()
                        binding.pg2.visibility = View.GONE
                        startActivity(Intent(this, LoginPage::class.java))
                    }
                    else{
                        binding.pg2.visibility = View.GONE
                        Toast.makeText(this, "${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this , gso)

        googleSignInClient.signOut()


        binding.imageViewGoogle1.setOnClickListener{
            binding.pg2.visibility = View.VISIBLE
            signInGoogle()
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
                    binding.pg2.visibility = View.GONE
                    val intent : Intent = Intent(this , UserInformation::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this , it.exception.toString() , Toast.LENGTH_SHORT).show()
                }
            }
    }

//    override fun onBackPressed() {
//        val intent = Intent(this@RegisterPage, LoginPage::class.java)
//        startActivity(intent)
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
//    }
}