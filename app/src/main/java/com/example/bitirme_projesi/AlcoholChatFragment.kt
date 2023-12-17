package com.example.bitirme_projesi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitirme_projesi.databinding.FragmentAlcoholChatBinding
import com.example.bitirme_projesi.databinding.FragmentCigaretteChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AlcoholChatFragment : Fragment() {

    private lateinit var database: FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    lateinit var binding: FragmentAlcoholChatBinding
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
        binding = FragmentAlcoholChatBinding.inflate(layoutInflater)

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

    }

    override fun onResume() {
        super.onResume()

        bringDatas()

    }

    fun bringDatas(){
        database.collection("alcoholChat")
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
                        database.collection("alcoholChat").add(postHashMap)
                    }
                }
            }

        }

    }


}