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
import com.parse.SaveCallback;

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

        group.setGroupname(groupName);
        group.setDescription(description);

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

                goMainActivity();
                Toast.makeText(CreateGroupActivity.this, "Group created Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainActivity()
    {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }
}