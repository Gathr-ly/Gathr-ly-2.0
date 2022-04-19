package com.herbert.gathr_ly

import android.content.Context
import android.service.autofill.TextValueSanitizer
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseUser

class NewFriendAdpater (val context: Context, val friends: MutableList<ParseUser>): RecyclerView.Adapter<NewFriendAdpater.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = friends.get(position)
        holder.bind(friend)
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val friendName: TextView
        val friendId: TextView
        init {
            friendName = itemView.findViewById(R.id.friendName)
            friendId = itemView.findViewById(R.id.friendId)
        }

        fun bind(friend : ParseUser){
            friendName.text = friend.username
            friendId.text = friend.objectId
        }
    }

    // Clean all elements of the recycler
    fun clear() {
        friends.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(nfList: List<ParseUser>) {
        friends.addAll(nfList)
        notifyDataSetChanged()
    }
}