package com.example.weekendactivity;

import android.text.Editable;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public static final String KEY_AUTHOR = "author";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_ACTIVITY = "activityToComment";

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE,parseFile);
    }

    public String getContent(){
        return getString(KEY_CONTENT);
    }

    public void setContent(String content){
        put(KEY_CONTENT, content);
    }
    public ParseUser getAuthor(){ return getParseUser(KEY_AUTHOR);
    }

    public void setAuthor(ParseUser author){
        put(KEY_AUTHOR, author);
    }

    public Activity getKeyActivity(){ return (Activity) getParseObject(KEY_ACTIVITY);}

    public void setKeyActivity(Activity activity){ put(KEY_ACTIVITY, activity);}

}
