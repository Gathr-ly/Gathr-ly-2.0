package com.herbert.gathr_ly

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseUser

class EventFilteredFriendAdapter (
        val context: Context, val filteredFriends: MutableList<ParseUser>
): RecyclerView.Adapter<EventFilteredFriendAdapter.ViewHolder>(){

    // Define listener member variable
    private var listener: OnItemClickListener? = null

    // Define the listener interface
    interface OnItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
    }

    // Define the method that allows the parent activity or fragment to define the listener
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_filtered_friends, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = filteredFriends.get(position)
        holder.bind(friend)
    }

    override fun getItemCount(): Int {
        return filteredFriends.size
    }

    class ViewHolder(itemView: View, clickListener: OnItemClickListener?): RecyclerView.ViewHolder(itemView){
        val friendName: TextView
        val friendId: TextView
        lateinit var user: ParseUser
        val addButton: ImageButton
        val clickListener = clickListener
        init {
            friendName = itemView.findViewById(R.id.friendName)
            friendId = itemView.findViewById(R.id.friendId)
            addButton = itemView.findViewById(R.id.addButton)
        }

        fun bind(friend : ParseUser){
            friendName.text = friend.username
            friendId.text = "#" + friend.objectId
            user = friend
            addButton.setOnClickListener {
                clickListener?.onItemClick(itemView, adapterPosition)
            }
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