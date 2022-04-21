package com.herbert.gathr_ly

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseQuery
import com.parse.ParseUser


class NewFriendActivity : AppCompatActivity() {

    lateinit var newfriendRecyclerView: RecyclerView
    lateinit var newfriendadapter: NewFriendAdpater

    var allNewFriend: MutableList<ParseUser> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_friend)

        newfriendRecyclerView = findViewById(R.id.newfriendRecycleView)
        newfriendadapter = NewFriendAdpater(this, allNewFriend)
        newfriendRecyclerView.adapter = newfriendadapter
        newfriendRecyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.nf_searchButton).setOnClickListener{
            val nf_username = findViewById<EditText>(R.id.nf_username).text.toString()
            val nf_userid = findViewById<EditText>(R.id.nf_userid).text.toString()
            if(nf_username == ParseUser.getCurrentUser().username || nf_userid == ParseUser.getCurrentUser().objectId){
                newfriendadapter.clear()
                Toast.makeText(this, "You cannot add yourself as a friend", Toast.LENGTH_SHORT).show()
            }
            else if(nf_username != "" || nf_userid != "") {
                queryNewFriend(nf_username, nf_userid)
            }
            else{
                newfriendadapter.clear()
                Toast.makeText(this, "No input", Toast.LENGTH_SHORT).show()
            }
        }
    }



    fun queryNewFriend(nf_username: String, nf_userid: String){
        //Specify which class to query
        val query: ParseQuery<ParseUser> = ParseUser.getQuery()

        if(nf_userid != "" && nf_userid != null) {
            query.whereEqualTo(ParseUser.KEY_OBJECT_ID, nf_userid)
        }
        if(nf_username != "" && nf_username != null){
            query.whereEqualTo("username", nf_username)
        }

        query.findInBackground { newFriends, e ->
            if (e == null) {
                if(newFriends == null || newFriends.size == 0){
                    Toast.makeText(this, "Invalid Username/UserId", Toast.LENGTH_SHORT).show()
                }
                else{
                    for (friend in newFriends) {
                        Log.i(TAG, "Username: " + friend.username + " UserId: " + friend.objectId)
                        newfriendadapter.clear()
                        newfriendadapter.addAll(newFriends)
                    }
                }
            } else {
                Log.e(TAG, "Error fetching User")
            }
        }
    }

    companion object{
        val TAG = "NewFriendActivity"
    }
}