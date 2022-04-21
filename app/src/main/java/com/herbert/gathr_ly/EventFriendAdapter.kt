package com.herbert.gathr_ly

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseUser

class EventFriendAdapter (val context: Context, val filteredFriends: MutableList<ParseUser>): RecyclerView.Adapter<EventFriendAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = filteredFriends.get(position)
        holder.bind(friend)
    }

    override fun getItemCount(): Int {
        return filteredFriends.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val friendName: TextView
        val friendId: TextView
        lateinit var user: ParseUser
        val addButton: ImageButton
        init {
            friendName = itemView.findViewById(R.id.friendName)
            friendId = itemView.findViewById(R.id.friendId)
            addButton = itemView.findViewById(R.id.imageButton)
            addButton.setOnClickListener { view ->

            }
        }

        fun bind(friend : ParseUser){
            friendName.text = friend.username
            friendId.text = friend.objectId
            user = friend
        }
    }

    // Clean all elements of the recycler
    fun clear() {
        filteredFriends.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(nfList: List<ParseUser>) {
        filteredFriends.addAll(nfList)
        notifyDataSetChanged()
    }
}