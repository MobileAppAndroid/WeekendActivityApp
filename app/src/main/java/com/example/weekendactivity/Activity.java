package com.example.weekendactivity;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Activity")
public class Activity extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_ACTIVITYSTATUS = "activityStatus";
    public static final String KEY_GROUPNOTIFIED = "groupNotified";

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION,description);
    }

    public ParseUser getAuthor(){
        return getParseUser(KEY_AUTHOR);
    }

    public void setAuthor(ParseUser author){
        put(KEY_AUTHOR,author);
    }

    public String getActivitystatus(){
        return getString(KEY_ACTIVITYSTATUS);
    }

    public void setActivityStatus(String activityStatus){
        put(KEY_ACTIVITYSTATUS,activityStatus);
    }

    public Group getGroupNotified(){
        return (Group) getParseObject(KEY_GROUPNOTIFIED);
    }

    public void setKeyGroupnotified(Group group){
        put(KEY_GROUPNOTIFIED,group);
    }

}
