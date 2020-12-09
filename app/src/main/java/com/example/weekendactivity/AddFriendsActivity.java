package com.example.weekendactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weekendactivity.fragments.GroupFragment;
import com.example.weekendactivity.fragments.ProfileFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class AddFriendsActivity extends AppCompatActivity
{
    int count = 0;
    Button btnDone;

    ListView search_users;
    ArrayAdapter<String> usersSearchAdapter;
    ArrayList<String> arrayUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        search_users = (ListView) findViewById(R.id.search_users);
        btnDone = findViewById(R.id.btnDone);

        this.setTitle("Add Friends");

        btnDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                goProfileFragment();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.search_icon)
        {
            Log.d("AddFriendsActivity", "Searching for users...");

            findUsers();

            SearchView searchView = (SearchView) item.getActionView();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    usersSearchAdapter.getFilter().filter(s);
                    return false;
                }
            });

        }

        return super.onOptionsItemSelected(item);
    }

    public void findUsers() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        final User currentUser = (User) ParseUser.getCurrentUser();
        // query.whereEqualTo("email", "email@example.com");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    // The query was successful, returns the users that matches
                    // the criterias.
                    arrayUsers = new ArrayList<>();

                    for(ParseUser user : users)
                    {
                        if (!currentUser.getUsername().equals(user.getUsername()))
                        {
                            arrayUsers.add(user.getUsername());
                        }

                        // Log.d("AddFriendsActivity", currentUser.getUsername());
                    }

                    usersSearchAdapter = new ArrayAdapter<>(
                            AddFriendsActivity.this,
                            android.R.layout.simple_list_item_1,
                            arrayUsers
                    );
                    search_users.setAdapter(usersSearchAdapter);

                    search_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Toast.makeText(AddFriendsActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                            count++;

                            Log.d("AddFriendsActivity", arrayUsers.get(i));
                        }
                    });


                } else {
                    // Something went wrong.
                    Log.e("AddFriendsActivity", "Cannot get users list", e);
                    Toast.makeText(AddFriendsActivity.this, "Issue with friends list", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void goProfileFragment()
    {
        Intent i = new Intent(this, ProfileFragment.class);
        startActivity(i);

        // finish();
    }

}