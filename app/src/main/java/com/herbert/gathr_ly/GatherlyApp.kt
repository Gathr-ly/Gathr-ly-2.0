package com.herbert.gathr_ly

import android.app.Application
import com.parse.Parse
import com.parse.ParseObject

class GatherlyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        ParseObject.registerSubclass(Post::class.java)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("LSL3RNEKy2ICWwp1hM0q8Afi5uATGp18DHqb09GX")
                .clientKey("he4QJMkz3wt8kumiNPNay1BqRzwMZqGpAqsZQFfI")
                .server("https://parseapi.back4app.com/")
                .build());
    }
}