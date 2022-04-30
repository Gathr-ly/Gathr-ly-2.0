package com.herbert.gathr_ly.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.herbert.gathr_ly.*
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

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
            queryFriends()
        }

        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        adapter = FriendHsAdapter(requireContext(), allFriends)

        adapter.setOnItemClickListener(object : FriendHsAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View?, position: Int) {
                val friend = (rvFriends.findViewHolderForAdapterPosition(position)
                        as FriendHsAdapter.ViewHolder).friend
                (activity as MainActivity).setCurrentFriendToDelete(friend)
                deleteDetailAction(itemView!!)
            }

        })

        rvFriends.adapter = adapter
        rvFriends.layoutManager = LinearLayoutManager(requireContext())


        queryFriends()

        view.findViewById<Button>(R.id.btNewFriend).setOnClickListener {
            val intent = Intent(this@FriendsFragment.requireContext(), NewFriendActivity ::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        queryFriends()
    }

    private lateinit var deletePopupView: View
    lateinit var deletePopupWindow: PopupWindow

    fun deleteDetailAction(view: View) {
        deletePopupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_delete_friend, null)
        val width = ConstraintLayout.LayoutParams.MATCH_PARENT
        val height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        deletePopupWindow = PopupWindow(deletePopupView, width, height, focusable)
        deletePopupWindow.showAtLocation(rvFriends, Gravity.CENTER, 0, 0)
        deletePopupWindow
    }

//    Moved to MainActivity
//    fun confirmDeleteAction(view: View) {
//        val query: ParseQuery<FriendH> = ParseQuery.getQuery(FriendH::class.java)
//        query.whereEqualTo(FriendH.KEY_USER_ID, ParseUser.getCurrentUser().objectId)
//        query.whereEqualTo(FriendH.KEY_FRIEND_ID, ParseUser.getCurrentUser().objectId)
//        query.getFirstInBackground() { friendH, e ->
//            if (e == null) {
//                friendH.deleteInBackground()
//                val query: ParseQuery<FriendH> = ParseQuery.getQuery(FriendH::class.java)
//                query.whereEqualTo(FriendH.KEY_USER_ID, ParseUser.getCurrentUser().objectId)
//                query.whereEqualTo(FriendH.KEY_FRIEND_ID, ParseUser.getCurrentUser().objectId)
//                query.getFirstInBackground() { friendH, e ->
//                    if (e == null) {
//                        friendH.deleteInBackground()
//                        Toast.makeText(requireContext(), "Deleted friend", Toast.LENGTH_SHORT).show()
//                        queryFriends()
//                    } else {
//                        Log.e(EventDetailActivity.TAG, "Error fetching event helpers")
//                        e.printStackTrace()
//                    }
//                }
//            } else {
//                Log.e(EventDetailActivity.TAG, "Error fetching friendH's")
//                e.printStackTrace()
//            }
//        }
//    }

    fun queryFriends() {
        val query: ParseQuery<FriendH> = ParseQuery.getQuery(FriendH::class.java)
        query.whereEqualTo(FriendH.KEY_USER_ID, ParseUser.getCurrentUser().objectId)
        query.include(FriendH.KEY_FRIEND)
        query.addDescendingOrder("createdAt")

        query.findInBackground { FriendHs, e ->
            if (e != null) {
                // Something has gone wrong
                Log.e(TAG, "Error fetching friends")
                e.printStackTrace()
            } else {
                adapter.clear()
                if (FriendHs != null) {
                    for (friend in FriendHs) {
                        Log.i(TAG, "Username: " + friend.getFriend()?.username)// FriendH.getUser()?.username)
                    }

                    allFriends.addAll(FriendHs)
                    adapter.notifyDataSetChanged()
                    swipeContainer.isRefreshing = false
                }
            }
        }
    }

    companion object {
        const val TAG = "FriendsFragment"
    }
}

