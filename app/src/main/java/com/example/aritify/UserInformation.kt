package com.example.aritify

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.aritify.databinding.ActivityUserInformationBinding
import com.example.aritify.dataclasses.ArtifyUser
import com.example.aritify.mvvm.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import kotlin.concurrent.timer

class UserInformation : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var binding : ActivityUserInformationBinding
    private  lateinit var imageBitmap : Bitmap
    private var selectedImg: Uri? = null
    private var imageWhenNotChanged: String? = null
    private lateinit var vm : ViewModel
    private var photoClicked : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.transparent)

        auth = FirebaseAuth.getInstance()
        vm = ViewModelProvider(this).get(ViewModel::class.java)


        vm.retrive_user_data{
            binding.uiNameText.setText(it.name.toString())
            binding.uiPhoneNumberText.setText(it.phone_number.toString())
            binding.uiDateOfBirthText.setText(it.DOB.toString())
            binding.uiAddressText.setText(it.address.toString())
            Picasso.get().load(it.profile_photo).into(binding.uiProfilePhoto)
            imageWhenNotChanged = it.profile_photo
        }

        binding.uiProfilePhoto.setOnClickListener {
            imageOptionDialogue()
        }

        binding.uiSaveButton.setOnClickListener {
            val nameInput = binding.uiNameText.text.toString()
            val phoneNumberInput = binding.uiPhoneNumberText.text.toString()
            val dobInput = binding.uiDateOfBirthText.text.toString()
            val addressInput = binding.uiAddressText.text.toString()

            binding.pg3.visibility = View.VISIBLE

            if (TextUtils.isEmpty(nameInput) || TextUtils.isEmpty(phoneNumberInput) || TextUtils.isEmpty(dobInput) || TextUtils.isEmpty(addressInput)) {
                Toast.makeText(this, "Cannot be Empty", Toast.LENGTH_SHORT).show()
                binding.pg3.visibility = View.GONE
                return@setOnClickListener
            }

            uploadImageToStorage()

            val intent = Intent(this , MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //clear call stack
            startActivity(intent)
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()

        auth.signOut()
    }

    fun uploadImageToStorage(){
        if (photoClicked == 1){
            auth = FirebaseAuth.getInstance()
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
            val data = baos.toByteArray()

            storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference

            val storagePath = storageRef.child("Photos/${auth.currentUser?.uid}.jpg")
            val uploadTask = storagePath.putBytes(data)

            uploadTask.addOnSuccessListener { it ->
                val task = it.metadata?.reference?.downloadUrl
                task?.addOnSuccessListener {
                    selectedImg = it
                    val user = ArtifyUser(binding.uiNameText.text.toString() ,
                        binding.uiPhoneNumberText.text.toString().toLong() ,
                        binding.uiDateOfBirthText.text.toString(),
                        binding.uiAddressText.text.toString(),
                        selectedImg.toString())

                    vm.upload_user_data(user)
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                }
                    ?.addOnFailureListener {
                        Toast.makeText(this, "task failed", Toast.LENGTH_SHORT).show()
                    }
                binding.pg3.visibility = View.GONE


            }
        }else if (photoClicked == 0){
            auth = FirebaseAuth.getInstance()
            val user = ArtifyUser(binding.uiNameText.text.toString() ,
                binding.uiPhoneNumberText.text.toString().toLong() ,
                binding.uiDateOfBirthText.text.toString(),
                binding.uiAddressText.text.toString(),
                imageWhenNotChanged.toString())

            vm.upload_user_data(user)
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
            binding.pg3.visibility = View.GONE

        }

    }

    private fun imageOptionDialogue() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog_select_image_options)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        dialog.findViewById<LinearLayout>(R.id.layoutTakePicture).setOnClickListener {
            fromcamera()
            dialog.dismiss()
        }

        dialog.findViewById<LinearLayout>(R.id.layoutSelectFromGallery).setOnClickListener {
            pickFromGallery()
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            //See description at declaration
        }

        dialog.show()
    }
    @SuppressLint("QueryPermissionsNeeded")
    private fun pickFromGallery() {
        val pickPictureIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (pickPictureIntent.resolveActivity(this.packageManager) != null) {
            startActivityForResult(pickPictureIntent, 2)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun fromcamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, 1)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        photoClicked = 1

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    imageBitmap = data?.extras?.get("data") as Bitmap
                    try {
                        binding.uiProfilePhoto.setImageBitmap(imageBitmap)
                    }catch (e: Exception){}
                }
                2 -> {
                    val imageUri = data?.data
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                    try {
                        binding.uiProfilePhoto.setImageBitmap(imageBitmap)
                    }catch (e :Exception){
                    }
                }
            }
        }
    }
}