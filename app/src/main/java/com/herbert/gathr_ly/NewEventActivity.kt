package com.herbert.gathr_ly

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseQuery
import com.parse.ParseUser
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView


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

    // Changed to query friends more easily with FriendH's
    private fun queryFromFriends(query : String) {
        filteredUsers.clear()
        val filteredFriends = mutableListOf<ParseUser>()

        val queryFriends: ParseQuery<FriendH> = ParseQuery.getQuery(FriendH::class.java)
        queryFriends.whereEqualTo(FriendH.KEY_USER_ID, ParseUser.getCurrentUser().objectId)
        queryFriends.include(FriendH.KEY_FRIEND)
        queryFriends.addDescendingOrder("createdAt")

        queryFriends.findInBackground { filtered, e ->
            if (e != null) {
                // Something has gone wrong
                Log.e(TAG, "Error fetching filtered friends")
                e.printStackTrace()
            } else {
                if (filtered == null || filtered.size == 0) {
                    Toast.makeText(this, "Invalid Username", Toast.LENGTH_SHORT).show()
                } else {
                    for (friendH in filtered) {
                        filteredFriends.add(friendH.getFriend()!!)
                        Log.i(TAG, "Username: " + friendH.getFriend()?.username + " UserId: " + friendH.objectId)
                    }
                    for (user in filteredFriends) {
                        if (user.username.startsWith(query, true)) {
                            filteredUsers.add(user)
                        }
                    }
                    filteredFriendAdapter!!.notifyDataSetChanged()
                }
            }
        }

//        val query1: ParseQuery<Friend> = ParseQuery.getQuery(Friend::class.java)
//        query1.include("user2")
//        query1.whereEqualTo("user1", ParseUser.getCurrentUser())
//
//        val query2: ParseQuery<Friend> = ParseQuery.getQuery(Friend::class.java)
//        query2.include("user1")
//        query2.whereEqualTo("user2", ParseUser.getCurrentUser())
//
//        query1.findInBackground { filtered, e ->
//            if (e == null) {
//                if (filtered == null || filtered.size == 0) {
//                    Toast.makeText(this, "Invalid Username", Toast.LENGTH_SHORT).show()
//                } else {
//                    for (friend in filtered) {
//                        Log.i(TAG, "Username: " + friend.getuser2()?.username + " UserId: " + friend.objectId)
//                        friend.getuser2()?.let { filteredFriends.add(it) }
//                    }
//                    query2.findInBackground { filtered, e ->
//                        if (e == null) {
//                            if (filtered == null || filtered.size == 0) {
//                                Toast.makeText(this, "Invalid Username", Toast.LENGTH_SHORT).show()
//                            } else {
//                                for (friend in filtered) {
//                                    Log.i(TAG, "Username: " + friend.getuser1()?.username + " UserId: " + friend.objectId)
//                                    friend.getuser1()?.let { filteredFriends.add(it) }
//                                }
//                                for (user in filteredFriends) {
//                                    if (user.username.startsWith(query, true)) {
//                                        filteredUsers.add(user)
//                                    }
//                                }
//                                filteredFriendAdapter!!.notifyDataSetChanged()
//                            }
//                        } else {
//                            Log.e(TAG, "Error fetching filtered friends")
//                        }
//                    }
//                }
//            } else {
//                Log.e(TAG, "Error fetching filtered friends")
//            }
//        }
    }

    private lateinit var calendar: MaterialCalendarView
    private val days = mutableListOf<CalendarDay>()
    private lateinit var popupView: View
    private lateinit var popupWindow: PopupWindow

    fun editDaysAction(view: View) {
        popupView = LayoutInflater.from(this).inflate(R.layout.popup_edit_days, null)
        calendar = popupView.findViewById(R.id.calendar)
        val width = ConstraintLayout.LayoutParams.WRAP_CONTENT
        val height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        for (date in days) {
            calendar.setDateSelected(date, true)
        }
    }

    fun saveDaysAction(view: View) {
        days.clear()
        days.addAll(calendar.selectedDates)
        popupWindow.dismiss()
        Toast.makeText(this, "Saved days", Toast.LENGTH_SHORT).show()
    }

    fun newEventAction(view: View) {
        if (etEventName.text.isNullOrBlank() || etDetails.text.isNullOrBlank() ||
                days.isEmpty() || addedUsers.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
        } else {
            val event = Event()
            event.setName(etEventName.text.toString())
            event.setDetails(etEventName.text.toString())
            event.setDays(days)
            event.setUsers(addedUsers)
            event.saveInBackground { e ->
                if (e != null) {
                    Log.e(TAG, "Error creating new event")
                    e.printStackTrace()
                    Toast.makeText(this, "Error creating new event", Toast.LENGTH_SHORT).show()
                } else {
                    Log.i(TAG, "Successfully created new event")
                    Toast.makeText(this, "Successfully created new event", Toast.LENGTH_SHORT).show()

                    val addedUsersPlusCreator = mutableListOf<ParseUser>()
                    addedUsersPlusCreator.addAll(addedUsers)
                    addedUsersPlusCreator.add(ParseUser.getCurrentUser())

                    for (user in addedUsersPlusCreator) {
                        val eventHelper = EventHelper()
                        eventHelper.setUser(user)
                        eventHelper.setEvent(event)
                        eventHelper.saveInBackground { e ->
                            if (e != null) {
                                Log.e(TAG, "Error creating event helper")
                                e.printStackTrace()
                            } else {
                                Log.i(TAG, "Created event helper for user: " + user.username)
                            }
                        }
                    }
                    finish()
                }
            }
        }
    }

    companion object {
        const val TAG = "NewEventActivity"
        const val KEY_EVENTS = "events"
    }

    // Create Event class
    // - also in Parse
    // Make newEventAction create and persist an event object
    // Make the events fragment show current user's events
    // - refreshable
    // Make detail view
    // - can add schedule
    // - shows overlapping times
}
