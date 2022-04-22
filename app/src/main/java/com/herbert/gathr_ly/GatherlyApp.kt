package com.herbert.gathr_ly

import android.app.Application
import com.parse.Parse
import com.parse.ParseObject
import com.parse.ParseUser

class GatherlyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        ParseObject.registerSubclass(Post::class.java)
        ParseObject.registerSubclass(Friend::class.java)
        ParseObject.registerSubclass(Event::class.java)
        ParseObject.registerSubclass(EventHelper::class.java)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("Cgu7vLlXeUJOQC8rs8quJ3NGNjfr6fxjMIHCuAFP")
                .clientKey("6n8n8Po68Try8djaRUwkfx5UsFz1Uu4rSKEcFxTe")
                .server("https://parseapi.back4app.com/")
                .build());
    }
}