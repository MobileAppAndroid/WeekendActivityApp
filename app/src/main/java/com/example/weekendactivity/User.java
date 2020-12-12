package com.example.weekendactivity;

import android.provider.ContactsContract;

import com.google.android.gms.common.util.Strings;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_SCREENNAME = "screenName";
    public static final String KEY_PROFILEIMAGE = "profileImage";
    public static final String KEY_ACTIVITIES = "activities";
    public static final String KEY_GROUPS = "groups";
    public static final String KEY_FRIENDS = "friends";

    public String getScreenname() { return getString(KEY_SCREENNAME);}

    public void setScreenname(String screenName) { put(KEY_SCREENNAME, screenName);}

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

    public List<String> getFriends(){
        List<String> result = getList(KEY_FRIENDS);
        if(result == null) {
            return new ArrayList<String>();
        }
        return result;
    }

   /* public void setFriends(List<User> users){
        List<String> friends = new ArrayList<>();
        for ( User user : users) {
            friends.add(user.getObjectId());
        }
        put(KEY_FRIENDS, friends); }  */

    public void addToFriends(User user){
        addUnique(KEY_FRIENDS, user.getObjectId());
    }

    public void deleteFromFriends(User user) {
        List<String> friends = getFriends();
        if(friends.contains(user.getObjectId())) {
            friends.remove(user.getObjectId());
            put(KEY_FRIENDS, friends);
        }
        else{ return; }
    }



}
