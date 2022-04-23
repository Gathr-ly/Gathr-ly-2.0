package com.herbert.gathr_ly.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.herbert.gathr_ly.*
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class EventsFragment : Fragment() {

    lateinit var rvEvents: RecyclerView
    lateinit var adapter: EventAdapter
    val allEvents = mutableListOf<Event>()
    lateinit var swipeContainer: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvEvents = view.findViewById(R.id.rvEvents)
        swipeContainer = view.findViewById(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener {
            Log.i(TAG, "Refreshing events")
            queryEvents()
        }

        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        adapter = EventAdapter(requireContext(), allEvents)
        rvEvents.adapter = adapter
        rvEvents.layoutManager = LinearLayoutManager(requireContext())

        queryEvents()
    }

    override fun onResume() {
        super.onResume()
        queryEvents()
    }

    private fun queryEvents() {
        val query: ParseQuery<EventHelper> = ParseQuery.getQuery(EventHelper::class.java)
        query.include(EventHelper.KEY_USER)
        query.include(EventHelper.KEY_EVENT)
        query.whereEqualTo(EventHelper.KEY_USER_ID, ParseUser.getCurrentUser().objectId)
        query.findInBackground { EventHelperList, e ->
            if (e != null) {
                Log.e(TAG, "Error fetching event helpers")
                e.printStackTrace()
            } else {
                allEvents.clear()
                for (eventHelper in EventHelperList) {
                    val event = eventHelper.getEvent()!!
                    Log.i(TAG, "Name: " + event.getName() + " , Details: " + event.getDetails())
                    allEvents.add(event)
                    adapter.notifyDataSetChanged()
                }
                Log.i(TAG, "finished querying events")
                swipeContainer.isRefreshing = false
            }
        }
    }

    companion object {
        const val TAG = "EventsFragment"
    }
}
