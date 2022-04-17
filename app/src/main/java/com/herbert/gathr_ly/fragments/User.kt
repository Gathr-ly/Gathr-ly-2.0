package com.herbert.gathr_ly
import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser


// Description: String
// Image: File
// User: User
@ParseClassName("User")
// TODO: "Post" is the name of your new class in Back4App..
// no internet at the moment
class User: ParseObject() {

    fun getUsername() : String? {
        return getString(KEY_USERNAME)
    }

    fun setUsername(username: String) {
        put(KEY_USERNAME,username)
    }

    fun getUserId() : String? {
        return objectId
    }
    //USER ID IS NOT SETTABLE, AUTOMATIC ASSIGNED BY SYSTEM

    companion object {
        const val KEY_USERNAME = "username"
        const val KEY_OBJECTID = "objectId"
    }

}