package com.example.aritify.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aritify.R
import com.example.aritify.databinding.FragmentHomeBinding
import com.example.aritify.databinding.FragmentMicBinding
import com.google.firebase.auth.FirebaseAuth

class MicFragment : Fragment(){

    private var _binding : FragmentMicBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMicBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.logoutButton.setOnClickListener {
            auth.signOut()
            activity?.finish()
        }
    }

}