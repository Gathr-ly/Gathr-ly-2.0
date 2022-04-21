package com.herbert.gathr_ly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseQuery
import com.parse.ParseUser

class NewEventActivity : AppCompatActivity() {

    private val addedUsers = mutableListOf<ParseUser>()
    private val filteredUsers = mutableListOf<ParseUser>()
    private lateinit var etEventName: EditText
    private lateinit var etDetails: EditText
    private lateinit var btDays: Button
    private lateinit var search: SearchView
    private lateinit var rvAddedUsers: RecyclerView
    private lateinit var rvFilteredUsers: RecyclerView
    private var addedUserAdapter: EventAddedFriendAdapter? = null
    private var filteredFriendAdapter: EventFilteredFriendAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_event)

        etEventName = findViewById(R.id.etEventName)
        etDetails = findViewById(R.id.etDetails)
        btDays = findViewById(R.id.btDays)
        search = findViewById(R.id.search)
        rvAddedUsers = findViewById(R.id.rvAddedUsers)
        rvFilteredUsers = findViewById(R.id.rvFilteredUsers)

        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    Log.i(TAG, "Queried: $query")
                    queryFromFriends(query)
                }
                search.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        addedUserAdapter = EventAddedFriendAdapter(this, addedUsers)
        addedUserAdapter!!.setOnItemClickListener(object : EventAddedFriendAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View?, position: Int) {
                val user = (rvAddedUsers.findViewHolderForAdapterPosition(position)
                        as EventAddedFriendAdapter.ViewHolder).user
                addedUserAdapter!!.addedFriends.remove(user)
                addedUserAdapter!!.notifyDataSetChanged()
                filteredFriendAdapter!!.filteredFriends.add(user)
                filteredFriendAdapter!!.notifyDataSetChanged()
            }
        })
        rvAddedUsers.adapter = addedUserAdapter
        rvAddedUsers.layoutManager = LinearLayoutManager(this)

        filteredFriendAdapter = EventFilteredFriendAdapter(this, filteredUsers)
        filteredFriendAdapter!!.setOnItemClickListener(object : EventFilteredFriendAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View?, position: Int) {
                val user = (rvFilteredUsers.findViewHolderForAdapterPosition(position)
                        as EventFilteredFriendAdapter.ViewHolder).user
                filteredFriendAdapter!!.filteredFriends.remove(user)
                filteredFriendAdapter!!.notifyDataSetChanged()
                addedUserAdapter!!.addedFriends.add(user)
                addedUserAdapter!!.notifyDataSetChanged()
            }
        })

        rvFilteredUsers.adapter = filteredFriendAdapter
        rvFilteredUsers.layoutManager = LinearLayoutManager(this)

    }

    private fun queryFromFriends(query : String) {
        filteredUsers.clear()
        val filteredFriends = mutableListOf<ParseUser>()

        val query1: ParseQuery<Friend> = ParseQuery.getQuery(Friend::class.java)
        query1.include("user2")
        query1.whereEqualTo("user1", ParseUser.getCurrentUser())

        val query2: ParseQuery<Friend> = ParseQuery.getQuery(Friend::class.java)
        query2.include("user1")
        query2.whereEqualTo("user2", ParseUser.getCurrentUser())

        query1.findInBackground { filtered, e ->
            if (e == null) {
                if (filtered == null || filtered.size == 0) {
                    Toast.makeText(this, "Invalid Username", Toast.LENGTH_SHORT).show()
                } else {
                    for (friend in filtered) {
                        Log.i(TAG, "Username: " + friend.getuser2()?.username + " UserId: " + friend.objectId)
                        friend.getuser2()?.let { filteredFriends.add(it) }
                    }
                    query2.findInBackground { filtered, e ->
                        if (e == null) {
                            if (filtered == null || filtered.size == 0) {
                                Toast.makeText(this, "Invalid Username", Toast.LENGTH_SHORT).show()
                            } else {
                                for (friend in filtered) {
                                    Log.i(TAG, "Username: " + friend.getuser1()?.username + " UserId: " + friend.objectId)
                                    friend.getuser1()?.let { filteredFriends.add(it) }
                                }
                                for (user in filteredFriends) {
                                    if (user.username.startsWith(query, true)) {
                                        filteredUsers.add(user)
                                    }
                                }
                                filteredFriendAdapter!!.notifyDataSetChanged()
                            }
                        } else {
                            Log.e(TAG, "Error fetching filtered friends")
                        }
                    }
                }
            } else {
                Log.e(TAG, "Error fetching filtered friends")
            }
        }



    }

    companion object {
        const val TAG = "NewEventActivity"
    }
}