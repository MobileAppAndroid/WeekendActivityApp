package com.example.weekendactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weekendactivity.fragments.GroupFragment;
import com.parse.ParseException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        this.setTitle("Create Group");

        etGroupName = findViewById(R.id.etGroupName);
        etDescription = findViewById(R.id.etDescription);
        btnCreateGroup = findViewById(R.id.btnCreateGroup);

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
        List<User> members=new ArrayList<>();
        members.add(currentUser);
        group.setMembers(members);

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

    private void goGroupFragment()
    {
        Intent i = new Intent(this, GroupFragment.class);
        startActivity(i);
        // finish();
    }
}