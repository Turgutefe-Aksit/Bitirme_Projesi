package com.example.bitirme_projesi.View

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bitirme_projesi.Adapter.CalendarBoxAdapter
import com.example.bitirme_projesi.R
import com.example.bitirme_projesi.databinding.FragmentDateBlockCigaretteCalendarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class DateBlockCigaretteCalendarFragment : Fragment() {

    lateinit var binding: FragmentDateBlockCigaretteCalendarBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var adapter: CalendarBoxAdapter
    lateinit var sharedPreferences: SharedPreferences
    var stateList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDateBlockCigaretteCalendarBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val boxlist = ArrayList<String>()

        for (i in 1..30) {
            boxlist.add("")
        }

        adapter = CalendarBoxAdapter(requireContext(), boxlist,"cigarette",stateList)
        binding.idGVcourses.adapter = adapter
        val currUserID = auth.currentUser!!.uid
        binding.FinishCigaretteCal.setOnClickListener {

            val postHashMap = hashMapOf<String, Any>()
            database.collection("CigaretteCalendars").document("$currUserID")
                .get()
                .addOnSuccessListener {
                    if (it.exists()){
                        val data = it.data
                        if (data != null){
                            for ((key,value) in data){
                                postHashMap[key] = value!!
                            }
                        }

                        //database.collection("CigaretteCalendarsFinis").document("Cigarette").set(postHashMap)
                        database.collection("CigaretteCalendarsFinished").add(postHashMap)
                    }
                }

            val user = auth.currentUser?.uid
            sharedPreferences.edit().putBoolean((user.toString()+"cigaratte"),false).apply()

            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val selectedFragment = DateBlockGROCigaretteFragment()
            fragmentTransaction.replace(R.id.calenderFrameLayout,selectedFragment).commit()
        }

    }

    override fun onResume() {
        super.onResume()
        bringStates()

    }

    fun bringStates() {

        val currUserID = auth.currentUser!!.uid


        database.collection("CigaretteCalendars")
            .addSnapshotListener { value, error ->
                if (value!=null){
                    println(value)
                    if (!value.isEmpty){
                        val docs = value.documents

                        stateList.clear()

                        for (doc in docs){
                            if (doc.id == "$currUserID"){
                                for (i in 1..30) {
                                    var data = doc.get("Day"+"$i" ) as String

                                    stateList.add(data)
                                }
                            }
                        }

                        adapter.notifyDataSetChanged()
                    }
                }
            }
    }

}