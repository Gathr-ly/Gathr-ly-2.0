package com.herbert.gathr_ly.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.herbert.gathr_ly.LoginActivity
import com.herbert.gathr_ly.R
import com.parse.ParseUser


class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.activity_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = ParseUser.getCurrentUser().username
        val textView : TextView = requireView().findViewById<TextView>(R.id.tv_username)
       textView.text = user.toString()

//        val eventbtn : Button = requireView().findViewById<Button>(R.id.btnevent)
//        eventbtn.setOnClickListener {
//            Log.e("Event btn","Inside" )
//            goToEventsPage()
//        }
//        val friendsbtn : Button = requireView().findViewById<Button>(R.id.btnfriends)
//        friendsbtn.setOnClickListener {
//            goToFriendsPage()
//            Toast.makeText(getActivity(),"Text!",Toast.LENGTH_SHORT).show();
//        }
        val logoutbtn : Button = requireView().findViewById<Button>(R.id.logoutbtn)
        logoutbtn.setOnClickListener {
            logout()
        }

    }

    private fun logout() {
        ParseUser.logOut()
        val currentUser = ParseUser.getCurrentUser() // this will now be null
        //goback to signinpage
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }

//    private fun goToFriendsPage() {
//
//        Toast.makeText(context,"in freinds!",Toast.LENGTH_SHORT).show();
//            var fragmentToShow: Fragment? = null
//            fragmentToShow = FriendsFragment()
//            if (fragmentToShow != null) {
//                val t = childFragmentManager.beginTransaction()
//                val mFrag: Fragment = FriendsFragment()
//                t.replace(R.id.bottom_navigation, mFrag)
//                t.commit()
//            }
//
//    }
//
//    private fun goToEventsPage() {
//        Toast.makeText(context,"in freinds!",Toast.LENGTH_SHORT).show();
//        var fragmentToShow: Fragment? = null
//        fragmentToShow = EventsFragment()
//        if (fragmentToShow != null) {
//            val t = childFragmentManager.beginTransaction()
//            val mFrag: Fragment = EventsFragment()
//            t.replace(R.id.bottom_navigation, mFrag)
//            t.commit()
//        }
//
//    }
}
