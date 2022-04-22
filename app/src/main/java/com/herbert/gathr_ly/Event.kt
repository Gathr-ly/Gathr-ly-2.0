package com.herbert.gathr_ly

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.json.JSONArray
import org.json.JSONObject
import java.util.regex.Pattern

// creator: ParseUser
// eventName: String
// eventDetails: String
// days: JsonArray of strings
// users: JsonArray of strings
// schedules: JsonObject (user object ID ->
//                          Json Object (day string ->
//                              Json boolean array for schedule) )

@ParseClassName("Event")
class Event: ParseObject() {

    init {
        put(KEY_CREATOR, ParseUser.getCurrentUser())
    }

    fun getCreator(): ParseUser? {
        return getParseUser(KEY_CREATOR)
    }

    fun getName(): String? {
        return getString(KEY_NAME)
    }

    fun setName(name: String) {
        put(KEY_NAME, name)
    }

    fun getDetails(): String? {
        return getString(KEY_DETAILS)
    }

    fun setDetails(details: String) {
        put(KEY_DETAILS, details)
    }

    fun getDays(): JSONArray? {
        return getJSONArray(KEY_DAYS)
    }

    fun setDays(days: MutableList<CalendarDay>) {
        val daysList = mutableListOf<String>()
        for (day in days) {
            daysList.add(calendarDayToString(day))
        }
        val daysJsonArray = JSONArray(daysList)
        put(KEY_DAYS, daysJsonArray)
    }

    fun getUsers(): JSONArray? {
        return getJSONArray(KEY_USERS)
    }

    fun setUsers(users: MutableList<ParseUser>) {
        val userJsonArray = JSONArray()
        val scheduleJsonObject = JSONObject()
        for (user in users) {
            userJsonArray.put(user.objectId)
            scheduleJsonObject.put(user.objectId, JSONObject())
        }
        put(KEY_USERS, userJsonArray)
        put(KEY_SCHEDULES, scheduleJsonObject)
    }

    fun getSchedules(): JSONObject? {
        return getJSONObject(KEY_SCHEDULES)
    }

    companion object {
        const val KEY_CREATOR = "creator"
        const val KEY_NAME = "name"
        const val KEY_DETAILS = "details"
        const val KEY_DAYS = "days"
        const val KEY_USERS = "users"
        const val KEY_SCHEDULES = "schedules"

        fun stringToCalendarDay(str: String): CalendarDay {
            val p = Pattern.compile("(\\d+), (\\d+), (\\d+)")
            val m = p.matcher(str)
            return CalendarDay.from(m.group(1).toInt(), m.group(2).toInt(), m.group(3).toInt())
        }

        fun calendarDayToString(day: CalendarDay): String {
            return String.format(String.format("%s, %s, %s", day.year, day.month, day.day))
        }
    }

}