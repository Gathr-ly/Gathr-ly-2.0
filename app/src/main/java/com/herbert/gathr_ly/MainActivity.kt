package com.herbert.gathr_ly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.herbert.gathr_ly.fragments.EventsFragment
import com.herbert.gathr_ly.fragments.FriendsFragment
import com.herbert.gathr_ly.fragments.ProfileFragment
import com.parse.ParseQuery
import com.parse.ParseUser

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        findViewById<Button>(R.id.btNewFriend).setOnClickListener{
//            val intent = Intent(this@MainActivity, NewFriendActivity::class.java)
//            startActivity(intent)
//        }
        val fragmentManager: FragmentManager = supportFragmentManager

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
                item ->

            var fragmentToShow: Fragment? = null
            var tag: String = ""
            when(item.itemId) {

                R.id.action_events -> {
                    fragmentToShow = EventsFragment()
                    tag = "EventsFragment"
                }

                R.id.action_friends -> {
                    fragmentToShow = FriendsFragment()
                    tag = "FriendsFragment"
                }

                R.id.action_profile -> {
                    fragmentToShow = ProfileFragment()
                    tag = "ProfileFragment"
                }
            }

            if (fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow, tag).commit()
            }

            // Return True to show that user has interacted with item
            true
        }

        // Set default selection
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_events



        // queryPosts()
    }

    fun newEventScreenAction(view: View) {
        val intent = Intent(this@MainActivity, NewEventActivity::class.java)
        startActivity(intent)
    }

    fun newFriendScreenAction(view: View) {
        val intent = Intent(this@MainActivity, NewFriendActivity::class.java)
        startActivity(intent)
    }

    private lateinit var currentFriendToDelete: ParseUser

    fun setCurrentFriendToDelete(friend: ParseUser) {
        currentFriendToDelete = friend
    }

    fun confirmDeleteFriendAction(view: View) {
        val query: ParseQuery<FriendH> = ParseQuery.getQuery(FriendH::class.java)
        query.whereEqualTo(FriendH.KEY_USER_ID, ParseUser.getCurrentUser().objectId)
        query.whereEqualTo(FriendH.KEY_FRIEND_ID, currentFriendToDelete.objectId)
        query.getFirstInBackground() { friendH, e ->
            if (e == null) {
                friendH.deleteInBackground()
                val query: ParseQuery<FriendH> = ParseQuery.getQuery(FriendH::class.java)
                query.whereEqualTo(FriendH.KEY_USER_ID, currentFriendToDelete.objectId)
                query.whereEqualTo(FriendH.KEY_FRIEND_ID, ParseUser.getCurrentUser().objectId)
                query.getFirstInBackground() { friendH, e ->
                    if (e == null) {
                        friendH.deleteInBackground()
                        Toast.makeText(this, "Deleted friend", Toast.LENGTH_SHORT).show()
                        val fragmentManager = supportFragmentManager
                        val friendsFragment: FriendsFragment = fragmentManager.findFragmentByTag("FriendsFragment") as FriendsFragment
                        friendsFragment.queryFriends()
                        friendsFragment.deletePopupWindow.dismiss()
                    } else {
                        Log.e(EventDetailActivity.TAG, "Error fetching event helpers")
                        e.printStackTrace()
                    }
                }
            } else {
                Log.e(EventDetailActivity.TAG, "Error fetching friendH's")
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
