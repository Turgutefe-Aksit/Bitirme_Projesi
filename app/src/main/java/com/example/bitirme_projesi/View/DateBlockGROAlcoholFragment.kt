package com.example.bitirme_projesi.View

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bitirme_projesi.R
import com.example.bitirme_projesi.databinding.FragmentDateBlockGROAlcoholBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DateBlockGROAlcoholFragment : Fragment() {
    private lateinit var database: FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: FragmentDateBlockGROAlcoholBinding
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())



        // Inflate the layout for this fragment
        binding = FragmentDateBlockGROAlcoholBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.alcoholStartCalendarbtn.setOnClickListener {
            startCigaretteCalendar()
        }

    }

    fun startCigaretteCalendar(){
        val user = auth.currentUser?.uid

        sharedPreferences.edit().putBoolean((user.toString()+"alcohol"),true).apply()
        val postHashMap = hashMapOf<String, Any>()
        for (i in 1..30) {
            postHashMap.put("Day"+"$i","2")


        }

        database.collection("AlcoholCalendars").document("$user").set(postHashMap)
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val selectedFragment = DateBlockAlcoholCalendarFragment()
        fragmentTransaction.replace(R.id.calenderFrameLayout,selectedFragment).commit()
    }



}