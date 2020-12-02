package com.example.weekendactivity;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Activity.class);
        ParseObject.registerSubclass(Group.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Comment.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("C1S9b9UgDwLwt5q007GvOuf5XPbsahomtrvsWwr2")
                .clientKey("bqCVEUqkpePX70dhg9OpC4y0Xx6ntnnHxhSIQ5Zt")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
