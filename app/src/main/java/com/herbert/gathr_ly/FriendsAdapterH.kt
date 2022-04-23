package com.herbert.gathr_ly

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FriendHsAdapter (val context: Context, val FriendHs: ArrayList<FriendH>):
    RecyclerView.Adapter<FriendHsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendHsAdapter.ViewHolder {
        // Specify the layout file to use for this item

        val view = LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendHsAdapter.ViewHolder, position: Int) {
        val friendH = FriendHs.get(position)
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var friendName: TextView
        var friendId: TextView

        init {
            friendName = itemView.findViewById<TextView>(R.id.friendName)
            friendId = itemView.findViewById<TextView>(R.id.friendId)
        }

        fun bind(friendH: FriendH) {
            friendName.text = friendH.getUser()?.username
            friendId.text = friendH.getUser()?.objectId

        }
    }
}