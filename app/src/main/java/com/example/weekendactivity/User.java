package com.example.weekendactivity;

import android.provider.ContactsContract;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_SCREENNAME = "screenName";
    public static final String KEY_PROFILEIMAGE = "profileImage";
    public static final String KEY_ACTIVITIES = "activities";
    public static final String KEY_GROUPS = "groups";

    public String getScreenname() { return getString(KEY_SCREENNAME);}

    public void setKeyScreenname(String screenName) { put(KEY_SCREENNAME, screenName);}

    public ParseFile getProfileImage(){ return getParseFile(KEY_PROFILEIMAGE); }

    public void setProfileImage(ParseFile profileImage){ put(KEY_PROFILEIMAGE, profileImage);}

    public List<String> getActivities(){ return getList(KEY_ACTIVITIES); }

    public void addToActivities(Activity activity){
        List<String> activities= getActivities();
        if (activities.contains(activity.getObjectId())){ return; }
        activities.add(activity.getObjectId());
        put(KEY_ACTIVITIES,activities);
    }

    public void deleteFromActivities(Activity activity) {
        List<String> activities= getActivities();
        if(activities.contains(activity.getObjectId())){
            activities.remove(activity.getObjectId());
        }
        put(KEY_ACTIVITIES,activities);
    }


    public List<String> getGroups(){ return getList(KEY_GROUPS); }

    public void addToGroups(Group group){
        List<String> groups = getGroups();
        if (groups.contains(group.getObjectId())){ return; }
        groups.add(group.getObjectId());
        put(KEY_GROUPS, groups);
    }

    public void deleteFromGroups(Group group) {
        List<String> groups= getGroups();
        if(groups.contains(group.getObjectId())){
            groups.remove(group.getObjectId());
        }
        put(KEY_GROUPS, groups);
    }



}
