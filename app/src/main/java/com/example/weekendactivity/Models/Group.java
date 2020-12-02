package com.example.weekendactivity.Models;

import java.util.List;

public class Group {
    private String groupId;
    private String groupName;
    private String description;
    private List<User> managers;
    private List<User> members;
    private List<Activity> activities;

    public Group(String groupId, String groupName){
        this.groupId = groupId;
        this.groupName = groupName;

    }

    public String getGroupId(){
        return groupId;
    }

    public String getGroupName(){
        return groupName;
    }
}
