package com.example.weekendactivity;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

@ParseClassName("Activity")
public class Activity extends ParseObject {

    private static final String KEY_ACTIVITYNAME = "activityName";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_GROUPNOTIFIED = "groupNotified";
    private static final String KEY_ACTIVITYSTATUS = "activityStatus";
    private static final String KEY_STARTDATE = "startDate";
    private static final String KEY_ENDDATE = "endDate";
    private static final String KEY_MEMBERREGISTERED = "memberRegistered";
    private static final String KEY_COMMENTS = "comments";

    private String getActivityname(){ return getString(KEY_ACTIVITYNAME); }

    public void setActivityname(String activityName){
        put(KEY_ACTIVITYNAME, activityName);
    }

    private String getDescription(){ return getString(KEY_DESCRIPTION); }

    public void setDescription(String description){
        put(KEY_DESCRIPTION,description);
    }

    public ParseUser getAuthor(){
        return getParseUser(KEY_AUTHOR);
    }

    public void setAuthor(ParseUser author){ put(KEY_AUTHOR,author); }

    public String getActivitystatus(){
        return getString(KEY_ACTIVITYSTATUS);
    }

    public void setActivityStatus(String activityStatus){ put(KEY_ACTIVITYSTATUS,activityStatus); }

    public Group getGroupNotified(){
        return (Group) getParseObject(KEY_GROUPNOTIFIED);
    }

    public void setGroupnotified(Group group){
        put(KEY_GROUPNOTIFIED,group);
    }

    public Date getStartDate(){ return getDate(KEY_STARTDATE);};

    public void setStartdate(Date startDate) { put(KEY_STARTDATE, startDate); }

    public Date getEndDate(){ return getDate(KEY_ENDDATE); }

    public void setEnddate(Date endDate) { put(KEY_ENDDATE, endDate); }

    public List<String> getMemberRegistered(){ return getList(KEY_MEMBERREGISTERED); }

    public void addToMemberregistered(User user){
        List<String> memberRegistered= getMemberRegistered();
        if (memberRegistered.contains(user.getObjectId())){ return; }
        memberRegistered.add(user.getObjectId());
        put(KEY_MEMBERREGISTERED,memberRegistered);
    }

    public void deleteFromMemberregistered(User user) {
        List<String> memberRegistered= getMemberRegistered();
        if(memberRegistered.contains(user.getObjectId())){
            memberRegistered.remove(user.getObjectId());
        }
        put(KEY_MEMBERREGISTERED,memberRegistered);
    }

    public List<String> getComments(){ return getList(KEY_COMMENTS);};

    public void addToComments(Comment comment){
        List<String> comments = getComments();
        comments.add(comment.getObjectId());
        put(KEY_COMMENTS, comment);
    }

}
