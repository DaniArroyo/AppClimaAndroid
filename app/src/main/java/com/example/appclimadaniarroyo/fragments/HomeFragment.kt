package com.example.appclimadaniarroyo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.appclimadaniarroyo.R

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v =  inflater.inflate(R.layout.fragment_home, container, false)
        var buttonLogin = v.findViewById<Button>(R.id.buttonLogin)
        var buttonRegister = v.findViewById<Button>(R.id.buttonRegister)

        buttonLogin.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
        buttonRegister.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

        val loginPreferences = this.requireActivity().getSharedPreferences("SHARED_PREF", AppCompatActivity.MODE_PRIVATE)
        if (loginPreferences.contains("USER")) { //How can I ask here?
            findNavController().navigate(R.id.action_homeFragment_to_ciudadesMenuFragment)
        } else {
        }

        return v
    }

}