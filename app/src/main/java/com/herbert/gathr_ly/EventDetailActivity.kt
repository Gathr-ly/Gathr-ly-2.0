package com.herbert.gathr_ly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
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
                initialPopup()
            } else {
                calendarDays.setDateSelected(date, false)
            }
        }
    }

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
        val width = ConstraintLayout.LayoutParams.WRAP_CONTENT
        val height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        overlapPopupWindow = PopupWindow(overlapPopupView, width, height, focusable)
        initialPopupWindow.dismiss()
        overlapPopupWindow.showAtLocation(calendarDays, Gravity.CENTER, 0, 0)
    }

    fun showSchedule(view: View) {
        editSchedulePopupView = LayoutInflater.from(this).inflate(R.layout.popup_edit_schedule, null)
        val width = ConstraintLayout.LayoutParams.WRAP_CONTENT
        val height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        editSchedulePopupWindow = PopupWindow(editSchedulePopupView, width, height, focusable)
        initialPopupWindow.dismiss()
        editSchedulePopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    private fun selectDays() {
//        initialPopupView.setBackgroundColor(R.color.black)
        for (day in daysList) {
            calendarDays.setDateSelected(day, true)
        }
    }
}