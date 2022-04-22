package com.herbert.gathr_ly

import android.os.Parcelable
import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.json.JSONArray
import org.json.JSONObject
import java.util.regex.Pattern
import kotlinx.parcelize.Parcelize

// creator: ParseUser
// creatorUsername: String
// eventName: String
// eventDetails: String
// days: JsonArray of strings
// users: JsonArray of strings
// usernames: JsonArray of strings
// schedules: JsonObject (user object ID ->
//                          Json Object (day string ->
//                              Json boolean array for schedule) )

@Parcelize
@ParseClassName("Event")
class Event: ParseObject(), Parcelable {

//    var myCreatorUsername: String = ""
//    var myName: String = ""
//    var myDetails: String = ""
//    var myUsernames: JSONArray = JSONArray()
//    var myDays: MutableList<String> = mutableListOf()
//
//    fun saveData() {
//        myCreatorUsername = getCreatorUsername()!!
//        myName = getName()!!
//        myDetails = getDetails()!!
//        myUsernames = getUsernames()!!
//        myDays = getDays()
//    }

    fun getCreator(): ParseUser? {
        return getParseUser(KEY_CREATOR)
    }

    fun setCreator() {
        put(KEY_CREATOR, ParseUser.getCurrentUser())
    }

    fun getCreatorUsername(): String? {
        return getString(KEY_CREATOR_USERNAME)
    }

    fun setCreatorUsername() {
        put(KEY_CREATOR_USERNAME, ParseUser.getCurrentUser().username)
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

    fun getDays(): MutableList<String> {
        val daysList = mutableListOf<String>()
        val jsonArray = getJSONArray(KEY_DAYS)
        for (i in 0 until jsonArray!!.length()) {
            daysList.add(jsonArray.getString(i))
        }
        return daysList
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
        val usersPlusCreator = mutableListOf<ParseUser>()
        usersPlusCreator.addAll(users)
        usersPlusCreator.add(ParseUser.getCurrentUser())

        val userJsonArray = JSONArray()
        val usernameJsonArray = JSONArray()
        val scheduleJsonObject = JSONObject()
        for (user in usersPlusCreator) {
            userJsonArray.put(user.objectId)
            usernameJsonArray.put(user.username)
            scheduleJsonObject.put(user.objectId, JSONObject())
        }
        put(KEY_USERS, userJsonArray)
        put(KEY_USERNAMES, usernameJsonArray)
        put(KEY_SCHEDULES, scheduleJsonObject)
        setCreator()
        setCreatorUsername()
    }

    fun getUsernames(): JSONArray? {
        return getJSONArray(KEY_USERNAMES)
    }

    fun getSchedules(): JSONObject? {
        return getJSONObject(KEY_SCHEDULES)
    }

    private var bestTimes: MutableList<String>? = null

    fun bestTimes(): MutableList<String>? {
        if (bestTimes == null) {
            bestTimes = bestTimesHelper()
        }
        return bestTimes
    }

    private fun bestTimesHelper(): MutableList<String> {
        val list = mutableListOf<String>()
        var max = 0
        for (i in 0 until getDays().size) {
            val day = getDays()[i]
            val countForDay = IntArray(24)
            for (j in 0 until getUsers()!!.length()) {
                val userId = getUsers()!!.getString(j)
                val schedulesForUser = getSchedules()!!.getJSONObject(userId)
                if (schedulesForUser.has(day)) {
                    val scheduleForDay = schedulesForUser.getJSONArray(day)
                    for (hour in 0 until scheduleForDay.length()) {
                        if (scheduleForDay.getBoolean(hour)) {
                            countForDay[hour]++
                        }
                    }
                }
            }
            val x = countForDay.maxOrNull()
            if (x!! >= max) {
                if (x > max) {
                    list.clear()
                    max = x
                }
                if (max >= 2) {
                    for (hour in countForDay.indices) {
                        if (countForDay[hour] == max && list.size < 3) {
                            list.add("$day - $hour:00")
                        }
                    }
                }
            }
            overlappingSchedules[day] = countForDay
        }
        return list
    }

    val overlappingSchedules: HashMap<String, IntArray> = HashMap()

    companion object {
        const val KEY_CREATOR = "creator"
        const val KEY_CREATOR_USERNAME = "creatorUsername"
        const val KEY_NAME = "name"
        const val KEY_DETAILS = "details"
        const val KEY_DAYS = "days"
        const val KEY_USERS = "users"
        const val KEY_USERNAMES = "usernames"
        const val KEY_SCHEDULES = "schedules"

        fun stringToCalendarDay(str: String): CalendarDay {
            val r = Regex("(\\d+)/(\\d+)/(\\d+)")
            val m = r.matchEntire(str)
            val groupValues = m?.groupValues
            return CalendarDay.from(groupValues!![3].toInt(), groupValues!![1].toInt(), groupValues!![2].toInt())
        }

        fun calendarDayToString(day: CalendarDay): String {
            return String.format(String.format("%s/%s/%s", day.month, day.day, day.year))
        }
    }
}
