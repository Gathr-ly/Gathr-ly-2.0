package com.herbert.gathr_ly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewManager
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseQuery
import com.parse.ParseUser
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.json.JSONArray

class EventDetailActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvCreator: TextView
    private lateinit var tvDetails: TextView
    private lateinit var tvUsers: TextView
    private lateinit var tvBestTimes: TextView
    private lateinit var calendarDays: MaterialCalendarView
    private lateinit var event: Event
    private val daysList = mutableListOf<CalendarDay>()
    private lateinit var btEdit: ImageButton
    private lateinit var btDelete: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        tvName = findViewById(R.id.tvName)
        tvCreator = findViewById(R.id.tvCreator)
        tvDetails = findViewById(R.id.tvDetails)
        tvUsers = findViewById(R.id.tvUsers)
        tvBestTimes = findViewById(R.id.tvBestTimes)
        calendarDays = findViewById(R.id.calendarDays)
//        btEdit = findViewById(R.id.btEdit)
        btDelete = findViewById(R.id.btDelete)

        loadData()
    }

    private fun loadData() {
        val query: ParseQuery<Event> = ParseQuery.getQuery(Event::class.java)
        query.include(Event.KEY_CREATOR)
        query.whereEqualTo(EventAdapter.KEY_OBJECT_ID, intent.getStringExtra(EventAdapter.KEY_OBJECT_ID))
        query.getFirstInBackground { _event, e ->
            if (e == null) {
                event = _event

                if (event.getCreator()!!.objectId != ParseUser.getCurrentUser().objectId) {
//                    (btEdit.parent as ViewManager).removeView(btEdit)
                    (btDelete.parent as ViewManager).removeView(btDelete)
                }

                tvName.text = "Name: " + event.getName()
                tvCreator.text = "Creator: " + event.getCreatorUsername()
                tvDetails.text = "Details: " + event.getDetails()

                var usernames = ""
                val usernamesJsonArray = event.getUsernames()
                for (i in 0 until usernamesJsonArray!!.length()) {
                    val username = usernamesJsonArray!!.getString(i)
                    usernames += if (usernames == "") {
                        username
                    } else {
                        ", $username"
                    }
                }
                tvUsers.text = "Usernames: " + usernames

                val bestTimesList = event.bestTimes()
                var result = "Best times: "
                for (time in bestTimesList!!) {
                    result += if (result == "Best times: ") {
                        time
                    } else {
                        ", $time"
                    }
                }
                if (result == "Best times: ") {
                    result = "No overlapping times."
                }
                tvBestTimes.text = result

                val dayStringList = event.getDays()
                for (i in 0 until dayStringList.size) {
                    daysList.add(Event.stringToCalendarDay(dayStringList[i]))
                }
                selectDays()
                calendarDays.setOnDateChangedListener { widget, _date, selected ->
                    selectDays()
                    if (daysList.contains(_date)) {
                        date = _date
                        initialPopup()
                    } else {
                        calendarDays.setDateSelected(_date, false)
                    }
                }
            } else {
                Log.e(TAG, "error fetching event")
                e.printStackTrace()
            }
        }
    }

    private lateinit var date: CalendarDay
    private lateinit var initialPopupView: View
    private lateinit var initialPopupWindow: PopupWindow
    private lateinit var overlapPopupView: View
    private lateinit var overlapPopupWindow: PopupWindow
    private lateinit var editSchedulePopupView: View
    private lateinit var editSchedulePopupWindow: PopupWindow

    private fun initialPopup() {
        initialPopupView = LayoutInflater.from(this).inflate(R.layout.popup_initial, null)
        val width = ConstraintLayout.LayoutParams.MATCH_PARENT
        val height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        initialPopupWindow = PopupWindow(initialPopupView, width, height, focusable)
        initialPopupWindow.showAtLocation(calendarDays, Gravity.CENTER, 0, 0)
    }

    fun showOverlaps(view: View) {
        overlapPopupView = LayoutInflater.from(this).inflate(R.layout.popup_overlaps, null)
        val width = ConstraintLayout.LayoutParams.MATCH_PARENT
        val height = ConstraintLayout.LayoutParams.MATCH_PARENT
        val focusable = true
        overlapPopupWindow = PopupWindow(overlapPopupView, width, height, focusable)
        initialPopupWindow.dismiss()
        overlapPopupWindow.showAtLocation(calendarDays, Gravity.CENTER, 0, 0)

        val tvDate: TextView = overlapPopupView.findViewById(R.id.tvDate)
        tvDate.text = Event.calendarDayToString(date)
        val rvOverlaps: RecyclerView = overlapPopupView.findViewById(R.id.rvOverlaps)

        val x = event.overlappingSchedules
        val counts: IntArray = x[Event.calendarDayToString(date)]!!
        val adapter = OverlapAdapter(overlapPopupView.context, counts, event.getUsers()!!.length())
        rvOverlaps.adapter = adapter
        rvOverlaps.layoutManager = LinearLayoutManager(this)
    }

    lateinit var hourBools: BooleanArray

    fun showSchedule(view: View) {
        editSchedulePopupView = LayoutInflater.from(this).inflate(R.layout.popup_edit_schedule, null)
        val width = ConstraintLayout.LayoutParams.MATCH_PARENT
        val height = ConstraintLayout.LayoutParams.MATCH_PARENT
        val focusable = true
        editSchedulePopupWindow = PopupWindow(editSchedulePopupView, width, height, focusable)
        initialPopupWindow.dismiss()
        editSchedulePopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        val tvDate: TextView = editSchedulePopupView.findViewById(R.id.tvDate)
        tvDate.text = Event.calendarDayToString(date)
        val rvEditSchedules: RecyclerView = editSchedulePopupView.findViewById(R.id.rvEditSchedules)

        var jsonArray: JSONArray? = null
        val mySchedules = event.getSchedules()!!.getJSONObject(ParseUser.getCurrentUser().objectId)
        if (mySchedules.has(Event.calendarDayToString(date))) {
            jsonArray = mySchedules?.getJSONArray(Event.calendarDayToString(date))
        }
        hourBools = BooleanArray(24)
        if (jsonArray != null) {
            for (i in 0 until jsonArray.length()) {
                hourBools[i] = jsonArray.getBoolean(i)
            }
        }

        val adapter = ScheduleAdapter(this, hourBools)
        adapter!!.setOnItemClickListener(object : ScheduleAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View?, position: Int) {
                val viewHolder = rvEditSchedules.findViewHolderForAdapterPosition(position)
                        as ScheduleAdapter.ViewHolder
                viewHolder.onClick()
            }
        })
        rvEditSchedules.adapter = adapter
        rvEditSchedules.layoutManager = LinearLayoutManager(this)
    }

    private fun selectDays() {
        for (day in daysList) {
            calendarDays.setDateSelected(day, true)
        }
    }

    fun backDetailAction(view: View) {
        finish()
    }

    fun backAction(view: View) {
        overlapPopupWindow.dismiss()
    }

    fun cancelAction(view: View) {
        editSchedulePopupWindow.dismiss()
    }

//    private lateinit var editEventPopupView: View
//    private lateinit var editEventPopupWindow: PopupWindow
//
//    fun editDetailAction(view: View) {
//        editSchedulePopupView = LayoutInflater.from(this).inflate(R.layout.activity_new_event, null)
//
//    }
//
//    // Save edited event info (must have same name because of xml property)
//    fun newEventAction(view: View) {
//
//    }

    private lateinit var deletePopupView: View
    private lateinit var deletePopupWindow: PopupWindow

    fun deleteDetailAction(view: View) {
        deletePopupView = LayoutInflater.from(this).inflate(R.layout.popup_delete, null)
        val width = ConstraintLayout.LayoutParams.MATCH_PARENT
        val height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        deletePopupWindow = PopupWindow(deletePopupView, width, height, focusable)
        deletePopupWindow.showAtLocation(calendarDays, Gravity.CENTER, 0, 0)
    }

    fun confirmDeleteAction(view: View) {
        val query: ParseQuery<EventHelper> = ParseQuery.getQuery(EventHelper::class.java)
        query.whereEqualTo(EventHelper.KEY_EVENT_ID, event.objectId)
        query.findInBackground { eventHelpers, e ->
            if (e == null) {
                for (eventHelper in eventHelpers) {
                    eventHelper.deleteInBackground()
                }
                event.deleteInBackground()
                Toast.makeText(this, "Deleted event", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Log.e(TAG, "Error fetching event helpers")
                e.printStackTrace()
            }
        }
    }

    fun saveAction(view: View) {
        val jsonBoolArray = JSONArray()
        for (hour in hourBools) {
            jsonBoolArray.put(hour)
        }
        val myID = ParseUser.getCurrentUser().objectId
        val scheduleObject = event.getSchedules()!!.getJSONObject(myID)
        scheduleObject.put(Event.calendarDayToString(date), jsonBoolArray)
        event.setSchedule(myID, scheduleObject)
        event.saveInBackground { e ->
            if (e == null) {
                editSchedulePopupWindow.dismiss()
                Toast.makeText(this, "Saved changes to schedule", Toast.LENGTH_SHORT).show()
                loadData()
            } else {
                Log.e(TAG, "error updating schedule")
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val TAG = "EventDetailActivity"
    }
}
