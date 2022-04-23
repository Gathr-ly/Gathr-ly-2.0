package com.herbert.gathr_ly.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.herbert.gathr_ly.*
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

class FriendsFragment : Fragment() {
    lateinit var rvFriends: RecyclerView
    lateinit var swipeContainer: SwipeRefreshLayout
//   TODO
    lateinit var adapter: FriendHsAdapter
    val allFriends: MutableList<FriendH> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvFriends = view.findViewById(R.id.rvFriends)
        swipeContainer = view.findViewById(R.id.swipeContainer2)

        swipeContainer.setOnRefreshListener {
            Log.i(TAG, "Refreshing friends")
        }

        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        rvFriends = view.findViewById(R.id.rvFriends)
        adapter = FriendHsAdapter(requireContext(), allFriends as ArrayList<FriendH>)
        rvFriends.adapter = adapter

        rvFriends.layoutManager = LinearLayoutManager(requireContext())




        queryFriends()

        view.findViewById<Button>(R.id.btNewFriend).setOnClickListener {
            val intent = Intent(this@FriendsFragment.requireContext(), NewFriendActivity ::class.java)
            startActivity(intent)
        }

    }



    open fun queryFriends() {
        adapter.clear()
        val query: ParseQuery<FriendH> = ParseQuery.getQuery(FriendH::class.java)
        query.include(FriendH.KEY_USERH)
        query.addDescendingOrder("createdAt")

        query.findInBackground(object : FindCallback<FriendH> {
            override fun done(FriendHs: MutableList<FriendH>?, e: ParseException?) {
                if (e != null) {
                    // Something has gone wrong
                    Log.e(TAG, "Error fetching posts")
                } else {
                    if (FriendHs != null) {
                        for (post in FriendHs) {
                            Log.i(TAG, "Username ")// FriendH.getUser()?.username)
                        }

                        allFriends.addAll(FriendHs)
                        adapter.notifyDataSetChanged()
                        swipeContainer.setRefreshing(false)
                    }
                }
            }

        })
    }

    companion object {
        const val TAG = "FriendsFragment"
    }
}

