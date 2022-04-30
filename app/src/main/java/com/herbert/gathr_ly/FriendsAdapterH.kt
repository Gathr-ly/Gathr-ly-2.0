package com.herbert.gathr_ly

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseUser

class FriendHsAdapter (val context: Context, val FriendHs: MutableList<FriendH>):
    RecyclerView.Adapter<FriendHsAdapter.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendHsAdapter.ViewHolder {
        // Specify the layout file to use for this item

        val view = LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: FriendHsAdapter.ViewHolder, position: Int) {
        val friendH = FriendHs[position]
        holder.bind(friendH)
    }

    override fun getItemCount(): Int {
        return FriendHs.size
    }

    fun clear() {
        FriendHs.clear()
        notifyDataSetChanged()
    }

    fun addAll(FriendHList: List<FriendH>) {
        FriendHs.addAll(FriendHList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View, clickListener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView){

        val friendName: TextView = itemView.findViewById(R.id.friendName)
        val friendId: TextView = itemView.findViewById(R.id.friendId)
        lateinit var friend: ParseUser
        val removeButton: ImageButton = itemView.findViewById(R.id.removeButton)
        val clickListener = clickListener

        fun bind(friendH: FriendH) {
            friend = friendH.getFriend()!!
            friendName.text = friend.username
            friendId.text = friend.objectId
            removeButton.setOnClickListener{
                clickListener?.onItemClick(itemView, adapterPosition)
            }
        }
    }
}