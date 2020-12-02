package com.example.weekendactivity;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("Group")
public class Group extends ParseObject {

    private static final String KEY_GROUPNAME = "groupName";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_MANAGERS = "managers";
    private static final String KEY_MEMBERS = "members";
    private static final String KEY_ACTIVITIES = "activities";

    public String getGroupname(){ return getString(KEY_GROUPNAME); }

    public void setGroupname(String groupName){ put(KEY_GROUPNAME, groupName); }

    public String getDescription(){ return getString(KEY_DESCRIPTION); }

    public void setDescription(String description){ put(KEY_DESCRIPTION, description); }

    public List<String> getManagers(){ return getList(KEY_MANAGERS); }

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

    public void addToMembers(User user){
        List<String> members = getMembers();
        if (members.contains(user.getObjectId())){ return; }
        members.add(user.getObjectId());
        put(KEY_MEMBERS,members);
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
}
