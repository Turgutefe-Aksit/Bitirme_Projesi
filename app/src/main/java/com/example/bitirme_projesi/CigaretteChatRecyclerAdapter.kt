package com.example.bitirme_projesi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bitirme_projesi.databinding.FragmentCigaretteChatBinding

class CigaretteChatRecyclerAdapter(val messageList : ArrayList<Message>) : RecyclerView.Adapter<CigaretteChatRecyclerAdapter.MessageHolder>() {

    class MessageHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {


        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_chat_row,parent,false)
        return MessageHolder(view)

    }

    override fun getItemCount(): Int {
        return messageList.size

    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {


        val message = messageList[position].mesaj

        val nickname = messageList[position].nickName


        val messageTextView = holder.itemView.findViewById<TextView>(R.id.recyclerMessageText)
        messageTextView.text = message

        val nickTextView = holder.itemView.findViewById<TextView>(R.id.recyclerNickText)
        nickTextView.text = nickname
    }
}