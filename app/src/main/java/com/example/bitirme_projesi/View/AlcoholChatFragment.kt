package com.example.bitirme_projesi.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitirme_projesi.Adapter.CigaretteChatRecyclerAdapter
import com.example.bitirme_projesi.Message
import com.example.bitirme_projesi.Model.ApiClient
import com.example.bitirme_projesi.Model.ApiService
import com.example.bitirme_projesi.databinding.FragmentAlcoholChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AlcoholChatFragment : Fragment() {

    private lateinit var database: FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    lateinit var binding: FragmentAlcoholChatBinding
    private lateinit var recyclerViewAdapter: CigaretteChatRecyclerAdapter
    var messageList = ArrayList<Message>()
    lateinit var postService: ApiService
    var messageInfo = ""

    //API
    data class RequestBodyExample(
        @SerializedName("text") val key1: String
    )

    data class ApiResponse(
        @SerializedName("received_data") val key1: String,
        @SerializedName("response") val key2: String

    )

    fun main(message: String) {

        postService = ApiClient.getClient().create(ApiService::class.java)
        var post = postService.postData(RequestBodyExample(message))

        post.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {

                if (response.isSuccessful) {
                    val model = response.body()?.key2
                    println("${response.body()}")
                    println("${model}")
                }
            }
        })
        
    }




    //MOBIL
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
            main(binding.cigaratteChatText.text.toString())
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
                            val id = doc.get("UserID") as String
                            val index = Message(message,nickName,id)

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

        binding.cigaratteChatText.text = null

    }


}