package com.herbert.gathr_ly

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("EventHelper")
class EventHelper: ParseObject() {

    fun getUserId(): String? {
        return getString(KEY_USER_ID)
    }

    fun setUserId(id: String) {
        put(KEY_USER_ID, id)
    }

    fun getUser(): ParseUser? {
        return getParseUser(KEY_USER)
    }

    fun setUser(user: ParseUser) {
        put(KEY_USER, user)
        setUserId(user.objectId)
    }

    fun getEvent(): Event? {
        return getParseObject(KEY_EVENT) as Event
    }

    fun setEvent(event: Event) {
        put(KEY_EVENT, event)
    }

    companion object {
        const val KEY_USER_ID = "userID"
        const val KEY_USER = "user"
        const val KEY_EVENT = "event"
    }
}
