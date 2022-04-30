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
        val view = LayoutInflater.from(context).inflate(R.layout.item_new_friend, parent, false)
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
                addNewFriend(friend)
            }
        }

        fun addNewFriend(user: ParseUser) {
            val query: ParseQuery<FriendH> = ParseQuery.getQuery(FriendH::class.java)
//            val pair : List<ParseUser> = listOf(user1, user2)
//            query.whereContainedIn(Friend.KEY_USER1, pair)
//            query.whereContainedIn(Friend.KEY_USER2, pair)
//            //query.whereEqualTo(Friend.KEY_USER1, user1)
//            //query.whereEqualTo(Friend.KEY_USER2, user2)

            query.whereEqualTo(FriendH.KEY_USER_ID, ParseUser.getCurrentUser().objectId)
            query.whereEqualTo(FriendH.KEY_FRIEND_ID, user.objectId)
            query.findInBackground() { newFriend, e ->
                if (e == null) {
                    if (newFriend.isNotEmpty()) {
                        Toast.makeText(
                            itemView.context,
                            "You are already friends with this user",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val friendH1 = FriendH()
                        friendH1.setUser(ParseUser.getCurrentUser())
                        friendH1.setFriend(user)

                        val friendH2 = FriendH()
                        friendH2.setUser(user)
                        friendH2.setFriend(ParseUser.getCurrentUser())

                        friendH1.saveInBackground { e ->
                            if (e != null) {
                                Log.e(TAG, "Error saving friend")
                                e.printStackTrace()
                                Toast.makeText(itemView.context, "Failed to add friend", Toast.LENGTH_SHORT).show()
                            } else {
                                Log.i(TAG, "Successfully added friend")
                                friendH2.saveInBackground { e ->
                                    if (e != null) {
                                        Log.e(TAG, "Error saving friend")
                                        e.printStackTrace()
                                        Toast.makeText(itemView.context, "Failed to add friend", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Log.i(TAG, "Successfully added friend")
                                        Toast.makeText(itemView.context, "Successfully added friend", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "Error fetching friend data")
                    e.printStackTrace()
                }
            }
//            query.findInBackground{friendpairs, e->
//                if(e != null){
//                    Log.e(TAG, "Error fetching Friend")
//                }
//                else{
//                    if(friendpairs.size == 0 || friendpairs == null){
//                        val friend = Friend()
//                        friend.setuser1(user1)
//                        friend.setuser2(user2)
//                        friend.saveInBackground{exception->
//                            if(exception != null){
//                                Log.e(TAG, "Error while saving friend")
//                                exception.printStackTrace()
//                                Toast.makeText(itemView.context, "Fail to add a friend", Toast.LENGTH_SHORT).show()
//                            }else {
//                                Log.i(TAG, "Successfully saved friend")
//                                Toast.makeText(itemView.context, "Successfully add a friend", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                    else{
//                        Toast.makeText(itemView.context, "You are already friends with this user", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
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