package com.herbert.gathr_ly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NewFriendActivity : AppCompatActivity() {

    lateinit var newfriendRecyclerView: RecyclerView
    lateinit var newfriendadapter: NewFriendAdpater

    var allNewFriend: MutableList<User> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_friend)

        newfriendRecyclerView = findViewById(R.id.newfriendRecycleView)
        newfriendadapter = NewFriendAdpater(this, allNewFriend)
        newfriendRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun queryNewFriend(){

    }
}