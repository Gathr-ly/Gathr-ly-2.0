package com.herbert.gathr_ly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
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
                TODO("Not yet implemented")
                queryFromFriends()

                search.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                TODO("Not yet implemented")
                return false
            }

        })

    }

    private fun queryFromFriends() {
        val query: ParseQuery<User> = ParseQuery.getQuery(User::class.java)
    }
}