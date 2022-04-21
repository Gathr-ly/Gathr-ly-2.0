package com.herbert.gathr_ly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.herbert.gathr_ly.fragments.EventsFragment
import com.herbert.gathr_ly.fragments.FriendsFragment
import com.herbert.gathr_ly.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentManager: FragmentManager = supportFragmentManager

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
                item ->

            var fragmentToShow: Fragment? = null
            when(item.itemId) {

                R.id.action_events -> {
                    fragmentToShow = EventsFragment()
                }

                R.id.action_friends -> {
                    fragmentToShow = FriendsFragment()
                }

                R.id.action_profile -> {
                    fragmentToShow = ProfileFragment()
                }
            }

            if (fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
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

    companion object {
        const val TAG = "MainActivity"
    }
}
