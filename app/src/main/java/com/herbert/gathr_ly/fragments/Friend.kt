package com.herbert.gathr_ly
import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser


//user1: ParseUser
//user2: ParseUser

@ParseClassName("Friend")
class Friend: ParseObject() {

    fun getuser1() : ParseUser? {
        return getParseUser(KEY_USER1)
    }

    fun setuser1(user1: ParseUser) {
        put(KEY_USER1,user1)
    }

    fun getuser2() : ParseUser?{
        return getParseUser(KEY_USER2)
    }

    fun setuser2(user2: ParseUser){
        put(KEY_USER2, user2)
    }

    companion object {
        const val KEY_USER1 = "user1"
        const val KEY_USER2 = "user2"
    }

}