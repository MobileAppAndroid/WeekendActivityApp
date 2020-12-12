package com.example.weekendactivity;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Group")
public class Group extends ParseObject {

    public static final String KEY_GROUPNAME = "groupName";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_MANAGERS = "managers";
    public static final String KEY_MEMBERS = "members";
    public static final String KEY_ACTIVITIES = "activities";
    public static final String KEY_GROUPIMAGE = "groupImage";

    public String getGroupname(){ return getString(KEY_GROUPNAME); }

    public void setGroupname(String groupName){ put(KEY_GROUPNAME, groupName); }

    public String getDescription(){ return getString(KEY_DESCRIPTION); }

    public void setDescription(String description){ put(KEY_DESCRIPTION, description); }

    public List<String> getManagers(){ return getList(KEY_MANAGERS); }

    public void setManagers( List<User> users) {
        List<String> managers = new ArrayList<>();
        for ( User user : users) {
            managers.add(user.getObjectId());
        }
        put(KEY_MANAGERS, managers);  }

    public void addToManagers(User user){
        List<String> managers = getManagers();
        if (managers.contains(user.getObjectId())){ return; }
        managers.add(user.getObjectId());
        put(KEY_MANAGERS,managers);
    }

    public void deleteFromManagers(User user) {
        List<String> managers = getManagers();
        if(managers.contains(user.getObjectId())) {
            managers.remove(user.getObjectId());
            put(KEY_MANAGERS, managers);
        }
        else{ return; }
    }

    public List<String> getMembers(){ return getList(KEY_MEMBERS); }

    public void setMembers(List<User> users){
        List<String> members = new ArrayList<>();
        for ( User user : users) {
            members.add(user.getObjectId());
        }
        put(KEY_MEMBERS, members); }

    public void addToMembers(User user){
        addUnique(KEY_MEMBERS, user.getUsername());
    }

    public void deleteFromMembers(User user) {
        List<String> members = getMembers();
        if(members.contains(user.getObjectId())) {
            members.remove(user.getObjectId());
            put(KEY_MEMBERS, members);
        }
        else{ return; }
    }

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

    public ParseFile getGroupImage(){ return getParseFile(KEY_GROUPIMAGE); }

    public void setGroupImage(ParseFile groupImage){ put(KEY_GROUPIMAGE, groupImage); }
}
