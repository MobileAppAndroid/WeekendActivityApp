package com.example.weekendactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class AddFriendsActivity extends AppCompatActivity
{
    int count = 0;
    Button btnDone;

    User currentUser;
    List<String> currentFriends;

    ListView lvUsers;
    ArrayAdapter<String> usersSearchAdapter;

    List<ParseUser> searchResults = new ArrayList<>();
    List<String> searchResultsUsernames = new ArrayList<>(); // display in listview

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        currentUser = (User) ParseUser.getCurrentUser();
        currentFriends = currentUser.getFriends();

        lvUsers = (ListView) findViewById(R.id.lvUsers);
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



        usersSearchAdapter = new ArrayAdapter<>(
                AddFriendsActivity.this,
                android.R.layout.simple_list_item_1,
                searchResultsUsernames
        );
        lvUsers.setAdapter(usersSearchAdapter);

        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                if (currentFriends.contains(searchResults.get(i).getObjectId()))
                {
                    // remove friend
                    currentFriends.remove(searchResults.get(i).getObjectId());
                    Toast.makeText(AddFriendsActivity.this, "Removed friend!", Toast.LENGTH_SHORT).show();
                    currentUser.put(User.KEY_FRIENDS, currentFriends);
                    currentUser.saveInBackground();
                } else {
                    // add friend
                    Toast.makeText(AddFriendsActivity.this, "Added friend!", Toast.LENGTH_SHORT).show();
                    currentFriends.add(searchResults.get(i).getObjectId());
                    currentUser.addToFriends((User) searchResults.get(i));
                    currentUser.saveInBackground();
                }
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

            SearchView searchView = (SearchView) item.getActionView();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    searchAndDisplayUsers(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    // usersSearchAdapter.getFilter().filter(s);
                    return false;
                }
            });

        }

        return super.onOptionsItemSelected(item);
    }

    public void searchAndDisplayUsers(String searchQuery) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContains(User.KEY_USERNAME, searchQuery);
        query.whereNotEqualTo(User.KEY_USERNAME, ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    // The query was successful, returns the users that matches
                    // the criterias.
                    searchResults = users;

                    searchResultsUsernames.clear();
                    for(ParseUser user : users) {
                        searchResultsUsernames.add(user.getUsername());
                    }

                    usersSearchAdapter.notifyDataSetChanged();
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
        //Intent i = new Intent(this, MainActivity.class);
        //startActivity(i);
        finish();
    }

}