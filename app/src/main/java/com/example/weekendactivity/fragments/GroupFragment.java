package com.example.weekendactivity.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weekendactivity.CreateGroupActivity;
import com.example.weekendactivity.Group;
import com.example.weekendactivity.GroupAdapter;
import com.example.weekendactivity.MainActivity;
import com.example.weekendactivity.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment
{
    public static final String TAG = "GroupFragment";
    private RecyclerView rvGroups;
    protected GroupAdapter groupAdapter;
    protected List<Group> allGroups;
    private FloatingActionButton floatingActionButton;

    public GroupFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        rvGroups = view.findViewById(R.id.rvGroups);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);

        allGroups = new ArrayList<>();
        groupAdapter = new GroupAdapter(getContext(), allGroups);

        rvGroups.setAdapter(groupAdapter);
        rvGroups.setLayoutManager(new LinearLayoutManager(getContext()));

        queryGroups();

        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), CreateGroupActivity.class);
                startActivity(i);
            }
        });
    }

    private void queryGroups()
    {
        ParseQuery<Group> query = ParseQuery.getQuery(Group.class);

        query.findInBackground(new FindCallback<Group>()
        {
            @Override
            public void done(List<Group> groups, ParseException e)
            {
                if (e != null)
                {
                    Log.e(TAG, "Issue with getting groups", e);
                    return;
                }

//                for (Group group : groups)
//                {
//                    Log.i(TAG, )
//                }

                allGroups.addAll(groups);
                groupAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onResume()
    {
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity()).setActionBarTitle("Groups");
    }
}