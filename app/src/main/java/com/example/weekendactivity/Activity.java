package com.example.weekendactivity;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Parcel(analyze={Activity.class})
@ParseClassName("Activity")
public class Activity extends ParseObject {

    public static final String KEY_ACTIVITYNAME = "activityName";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_GROUPNOTIFIED = "groupNotified";
    public static final String KEY_ACTIVITYSTATUS = "activityStatus";
    public static final String KEY_STARTDATE = "startDate";
    public static final String KEY_ENDDATE = "endDate";
    public static final String KEY_MEMBERREGISTERED = "memberRegistered";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_COMMENTS = "comments";

    public String getActivityname(){ return getString(KEY_ACTIVITYNAME); }

    public void setActivityname(String activityName){
        put(KEY_ACTIVITYNAME, activityName);
    }

    public String getDescription(){ return getString(KEY_DESCRIPTION); }

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

    public String getLocation(){ return getString(KEY_LOCATION); }

    public void setLocation(String location){ put(KEY_LOCATION, location); }

    public Date getStartDate(){ return getDate(KEY_STARTDATE);};

    public void setStartdate(Date startDate) { put(KEY_STARTDATE, startDate); }

    public Date getEndDate(){ return getDate(KEY_ENDDATE); }

    public void setEnddate(Date endDate) { put(KEY_ENDDATE, endDate); }

    public List<String> getMemberRegistered(){ return getList(KEY_MEMBERREGISTERED); }

    public void setMemberRegistered(List<User> users){
        List<String> memberRegistered = new ArrayList<>();
        for ( User user : users) {
            memberRegistered.add(user.getObjectId());
        }
        put(KEY_MEMBERREGISTERED, memberRegistered);
    }

    public void addToMemberregistered(User user){
        List<String> memberRegistered= getMemberRegistered();
        if (memberRegistered.contains(user.getObjectId())){ return; }
        memberRegistered.add(user.getObjectId());
        put(KEY_MEMBERREGISTERED,memberRegistered);
    }

    public Boolean IsRegistered(User user){
        List<String> memberRegistered= getMemberRegistered();
        if (memberRegistered.contains(user.getObjectId())){ return Boolean.TRUE; }
        else{ return Boolean.FALSE; }
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
        List<String> comments;
        if( getComments() != null){
            comments = getComments();
        }
        else {
            comments = new ArrayList<>();
        }
        comments.add(comment.getObjectId());
        put(KEY_COMMENTS, comments);

    }

    public void addToComments(String commentId){
        List<String> comments;
        if( getComments() != null ){
            comments = getComments();

        }else {
            comments = new ArrayList<>();
        }
        comments.add(commentId);
        put(KEY_COMMENTS, comments);
    }

}
