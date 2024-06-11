package com.example.bitirme_projesi.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bitirme_projesi.Message
import com.example.bitirme_projesi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class CigaretteChatRecyclerAdapter(val messageList : ArrayList<Message>) : RecyclerView.Adapter<CigaretteChatRecyclerAdapter.MessageHolder>() {

    var firebaseUser:FirebaseUser? = null
    private val messageTypeLeft = 1
    private val messageTypeRight = 0


    class MessageHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {

        if (viewType == messageTypeRight){
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.recycler_rightchat_row,parent,false)
            return MessageHolder(view)
        }
        else{
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.recycler_chat_row,parent,false)
            return MessageHolder(view)
        }


    }

    override fun getItemCount(): Int {
        return messageList.size

    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {

        val id = messageList[position].id
        val message = messageList[position].mesaj
        val nickname = messageList[position].nickName


        val messageTextView = holder.itemView.findViewById<TextView>(R.id.recyclerMessageText)
        messageTextView.text = message

        val nickTextView = holder.itemView.findViewById<TextView>(R.id.recyclerNickText)
        nickTextView.text = nickname
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (messageList[position].id == firebaseUser!!.uid.toString()){
            return messageTypeRight
        }else{
            return messageTypeLeft
        }

    }

}