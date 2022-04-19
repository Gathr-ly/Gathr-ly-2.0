package com.herbert.gathr_ly

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.FindCallback
import com.parse.ParseException
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
            queryNewFriend()
        }
    }



    fun queryNewFriend(){
        //Specify which class to query
        val query: ParseQuery<ParseUser> = ParseUser.getQuery()

        query.findInBackground { newFriends, e ->
            if (e == null) {
                for (friend in newFriends) {
                    Log.i(TAG, "Username: " + friend.username + "UserId" + friend.objectId)
                    //newfriendadapter.clear()
                    //newfriendadapter.addAll(newFriends)
                }
            } else {
                Log.e(TAG, "Error fetching posts")
            }
        }


        /*
        val query: ParseQuery<User> = ParseQuery.getQuery(User::class.java)
        query.include(User.KEY_USERNAME)
        query.include(User.KEY_OBJECTID)

        if(nf_userid != "") {
            query.whereEqualTo(User.KEY_OBJECTID, nf_userid)
        }
        if(nf_username != ""){
            query.whereEqualTo(User.KEY_USERNAME, nf_username)
        }

        query.findInBackground(object: FindCallback<ParseUser>{
            override fun done(newFriends: MutableList<ParseUser>?, e: ParseException?) {
                if(e != null){
                    //Something went wrong
                    Log.e(TAG, "Error fetching posts")
                }
                else{
                    if (newFriends != null){
                        for(friend in newFriends){
                            Log.i(TAG, "Username: " + friend.username + "UserId"+ friend.objectId)
                        }
                        newfriendadapter.clear()
                        newfriendadapter.addAll(newFriends)
                    }
                }
            }
        })
        */
    }

    companion object{
        val TAG = "NewFriendActivity"
    }
}