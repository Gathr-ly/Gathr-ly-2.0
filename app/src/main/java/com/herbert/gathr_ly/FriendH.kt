package com.herbert.gathr_ly

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("FriendH")
// UserH
class FriendH: ParseObject() {

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

    fun getFriendId(): String? {
        return getString(KEY_FRIEND_ID)
    }

    fun setFriendId(id: String) {
        put(KEY_FRIEND_ID, id)
    }

    fun getFriend(): ParseUser? {
        return getParseObject(KEY_FRIEND) as ParseUser
    }

    fun setFriend(friend: ParseUser) {
        put(KEY_FRIEND, friend)
        setFriendId(friend.objectId)
    }

    companion object {
        const val KEY_USER_ID = "userID"
        const val KEY_USER = "user"
        const val KEY_FRIEND_ID = "friendID"
        const val KEY_FRIEND = "friend"
    }
}