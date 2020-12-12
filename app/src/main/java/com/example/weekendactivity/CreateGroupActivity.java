package com.example.weekendactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.weekendactivity.fragments.GroupFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupActivity extends AppCompatActivity
{
    public static final String TAG = "CreateGroupActivity";
    private EditText etGroupName;
    private EditText etDescription;
    private Button btnCreateGroup;

    private Spinner spFriendsList;
    User currentUser;
    List<String> currentFriends;
    ArrayAdapter<String> friendsAdapter;

    Group group;
    List<String> currentMembers;
    List<ParseUser> searchMembers = new ArrayList<>();
    List<String> searchMembersUsernames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        this.setTitle("Create Group");

        etGroupName = findViewById(R.id.etGroupName);
        etDescription = findViewById(R.id.etDescription);
        btnCreateGroup = findViewById(R.id.btnCreateGroup);
        spFriendsList = findViewById(R.id.spFriendsList);

        currentUser = (User) ParseUser.getCurrentUser();
        currentFriends = new ArrayList<>();
        currentFriends.add("Add Friends to groups");
        currentFriends = currentUser.getFriends();

        group = new Group();
        group.addToMembers((User) currentUser);
        currentMembers = group.getMembers();

        friendsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, currentFriends);
        friendsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFriendsList.setAdapter(friendsAdapter);

        spFriendsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!currentMembers.contains(currentFriends.get(i)))
                {
                    // add member
                    Toast.makeText(CreateGroupActivity.this, "Added member!", Toast.LENGTH_SHORT).show();
                    currentMembers.add(currentFriends.get(i));
//                    group.addToMembers((User) searchMembers.get(i));
                    group.saveInBackground();

                } else {
                    // remove member
                    currentMembers.remove(currentFriends.get(i));
                    Toast.makeText(CreateGroupActivity.this, "Removed member!", Toast.LENGTH_SHORT).show();
                    group.put(Group.KEY_MEMBERS, currentMembers);
                    group.saveInBackground();
                }
//                    Toast.makeText(CreateGroupActivity.this, "Clicked member!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.i(TAG, "onClick create group button");

                createGroup();
            }
        });

    }

    private void createGroup()
    {
        Group group = new Group();

        String groupName = etGroupName.getText().toString();
        String description = etDescription.getText().toString();

        User currentUser = (User) ParseUser.getCurrentUser();
        group.setGroupname(groupName);
        group.setDescription(description);
        //group.addToMembers(currentUser);
        //group.addToManagers(currentUser);
//        List<User> members=new ArrayList<>();
//        members.add(currentUser);
//        group.setMembers(members);

        friendsAdapter.notifyDataSetChanged();

        group.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e)
            {
                if (e != null)
                {
                    Log.e(TAG, "Issue with creating group", e);
                    Toast.makeText(CreateGroupActivity.this, "Failed to create group", Toast.LENGTH_SHORT).show();
                    return;
                }


                Toast.makeText(CreateGroupActivity.this, "Group created Successfully", Toast.LENGTH_SHORT).show();
                goGroupFragment();
            }
        });
    }

    public void searchAndDisplayUsers(String searchQuery) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContains(User.KEY_USERNAME, searchQuery);
        query.whereNotEqualTo(User.KEY_USERNAME, ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    // The query was successful, returns the users that matches
                    // the criteria.
                    searchMembers = users;

                    searchMembersUsernames.clear();
                    for(ParseUser user : users) {
                        searchMembersUsernames.add(user.getUsername());
                    }

                } else {
                    // Something went wrong.
                    Log.e("AddFriendsActivity", "Cannot get users list", e);
                    Toast.makeText(CreateGroupActivity.this, "Issue with friends list", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void goGroupFragment()
    {
        Intent i = new Intent(this, GroupFragment.class);
        startActivity(i);
        // finish();
    }
}