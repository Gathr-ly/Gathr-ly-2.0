package com.herbert.gathr_ly

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("FriendH")
// UserH
class FriendH: ParseObject() {
    fun getUser(): ParseUser? {
        return getParseUser(FriendH.KEY_USERH)
    }

    companion object {
        const val KEY_USERH = "user"
    }
}