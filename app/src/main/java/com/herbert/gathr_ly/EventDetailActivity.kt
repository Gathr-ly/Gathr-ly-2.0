package com.herbert.gathr_ly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

class EventDetailActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvCreator: TextView
    private lateinit var tvDetails: TextView
    private lateinit var tvUsers: TextView
    private lateinit var calendarDays: MaterialCalendarView
    private lateinit var eventBundle: Bundle
    private val daysList = mutableListOf<CalendarDay>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        tvName = findViewById(R.id.tvName)
        tvCreator = findViewById(R.id.tvCreator)
        tvDetails = findViewById(R.id.tvDetails)
        tvUsers = findViewById(R.id.tvUsers)
        calendarDays = findViewById(R.id.calendarDays)

        eventBundle = intent.getParcelableExtra<Bundle>("bundle") as Bundle

        tvName.text = "Name: " + eventBundle.getString(Event.KEY_NAME)
        tvCreator.text = "Creator: " + eventBundle.getString(Event.KEY_CREATOR_USERNAME)
        tvDetails.text = "Details: " + eventBundle.getString(Event.KEY_DETAILS)
        tvUsers.text = "Users: " + eventBundle.getString(Event.KEY_USERNAMES)

        val dayStringList = eventBundle.getStringArrayList(Event.KEY_DAYS)
        if (dayStringList != null) {
            for (i in 0 until dayStringList.size) {
                daysList.add(Event.stringToCalendarDay(dayStringList[i]))
            }
        }
        selectDays()
        calendarDays.setOnDateChangedListener { widget, date, selected ->
            selectDays()
            if (daysList.contains(date)) {
                Toast.makeText(this, "bingo", Toast.LENGTH_SHORT).show()
            } else {
                calendarDays.setDateSelected(date, false)
            }
        }
    }

    private fun selectDays() {
        for (day in daysList) {
            calendarDays.setDateSelected(day, true)
        }
    }
}