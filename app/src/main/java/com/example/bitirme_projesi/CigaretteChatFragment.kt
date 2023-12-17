package com.example.bitirme_projesi

import android.app.Notification
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil.findBinding
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.adapters.ViewGroupBindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bitirme_projesi.databinding.ActivityRegisterBinding
import com.example.bitirme_projesi.databinding.FragmentCigaretteChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.InternalHelpers
import com.google.firebase.database.Query
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.*
import com.google.firebase.firestore.Query.Direction.DESCENDING
import java.sql.Timestamp
import kotlin.time.TimeSource


class CigaretteChatFragment : Fragment() {

    private lateinit var database: FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    lateinit var binding: FragmentCigaretteChatBinding
    private lateinit var recyclerViewAdapter: CigaretteChatRecyclerAdapter
    var messageList = ArrayList<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        binding = FragmentCigaretteChatBinding.inflate(layoutInflater)
        //return inflater.inflate(R.layout.fragment_cigarette_chat, container, false)

        val layoutManager = LinearLayoutManager(context)
        layoutManager.reverseLayout = true
        binding.recyclerviewCigaratteChatLog.layoutManager = layoutManager
        recyclerViewAdapter = CigaretteChatRecyclerAdapter(messageList)
        binding.recyclerviewCigaratteChatLog.adapter = recyclerViewAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cigaratteChatButton.setOnClickListener{
            sendDataToCigarette()
        }

        //binding.recyclerviewCigaratteChatLog.layoutManager = LinearLayoutManager(context)
        //val chatAdapter = CigaretteChatRecyclerAdapter(messageList)
        //binding.recyclerviewCigaratteChatLog.adapter = chatAdapter


    }

    override fun onResume() {
        super.onResume()

        //recyclerViewAdapter = CigaretteChatRecyclerAdapter(messageList)
        //binding.recyclerviewCigaratteChatLog.adapter = recyclerViewAdapter
        bringDatas()

    }



    fun bringDatas(){
        database.collection("cigaretteChat")
            .orderBy("DateTime",com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
            if (value!=null){
                if (!value.isEmpty){
                    val docs = value.documents

                    messageList.clear()

                    for (doc in docs){
                        val nickName = doc.get("NickName") as String
                        val message = doc.get("Message") as String
                        val index = Message(message,nickName)
                        println(index.mesaj)
                        messageList.add(index)
                    }

                    recyclerViewAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    fun sendDataToCigarette(){
        val currUserID = auth.currentUser!!.uid
        val time = com.google.firebase.Timestamp.now()
        val message = binding.cigaratteChatText.text.toString()

        database.collection("users/$currUserID/info").addSnapshotListener { value, error ->
            if (value != null){
                if (!value.isEmpty){
                    val docs = value.documents

                    for (doc in docs){
                        val nickname = doc.get("NickName") as String

                        val postHashMap = hashMapOf<String, Any>()
                        postHashMap.put("NickName",nickname)
                        postHashMap.put("UserID",currUserID)
                        postHashMap.put("DateTime",time)
                        postHashMap.put("Message",message)
                        database.collection("cigaretteChat").add(postHashMap)
                    }
                }
            }

        }

    }
}

