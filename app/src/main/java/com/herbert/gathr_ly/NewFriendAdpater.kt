package com.herbert.gathr_ly

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseQuery
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
        val addButton: ImageButton
        init {
            friendName = itemView.findViewById(R.id.friendName)
            friendId = itemView.findViewById(R.id.friendId)
            addButton = itemView.findViewById(R.id.addButton)
        }

        fun bind(friend : ParseUser){
            friendName.text = friend.username
            friendId.text = "#" + friend.objectId
            addButton.setOnClickListener{
                addNewFriend(ParseUser.getCurrentUser(), friend)
            }
        }

        fun addNewFriend(user1: ParseUser, user2: ParseUser) {
            val query: ParseQuery<Friend> = ParseQuery.getQuery(Friend::class.java)
            val pair : List<ParseUser> = listOf(user1, user2)
            query.whereContainedIn(Friend.KEY_USER1, pair)
            query.whereContainedIn(Friend.KEY_USER2, pair)
            //query.whereEqualTo(Friend.KEY_USER1, user1)
            //query.whereEqualTo(Friend.KEY_USER2, user2)
            query.findInBackground{friendpairs, e->
                if(e != null){
                    Log.e(TAG, "Error fetching Friend")
                }
                else{
                    if(friendpairs.size == 0 || friendpairs == null){
                        val friend = Friend()
                        friend.setuser1(user1)
                        friend.setuser2(user2)
                        friend.saveInBackground{exception->
                            if(exception != null){
                                Log.e(NewFriendAdpater.TAG, "Error while saving friend")
                                exception.printStackTrace()
                                Toast.makeText(itemView.context, "Fail to add a friend", Toast.LENGTH_SHORT).show()
                            }else {
                                Log.i(NewFriendAdpater.TAG, "Successfully saved friend")
                                Toast.makeText(itemView.context, "Successfully add a friend", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else{
                        Toast.makeText(itemView.context, "You are already be friend with this user", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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

    companion object{
        val TAG = "NewFriendAdpater"
    }
}