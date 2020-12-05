package com.example.weekendactivity.fragments;

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

import com.example.weekendactivity.Activity;
import com.example.weekendactivity.ActivityAdapter;
import com.example.weekendactivity.R;
import com.example.weekendactivity.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityFragment extends Fragment {

    public static final String TAG = "ActivityFragment";
    private RecyclerView rvActivities;
    private ActivityAdapter adapter;
    private List<Activity> allActivities;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivityFragment newInstance(String param1, String param2) {
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvActivities = view.findViewById(R.id.rvActivities);

        allActivities = new ArrayList<>();

        adapter = new ActivityAdapter(getContext(), allActivities);

        // Steps to use the recycler view
        // 0. create layout for one row in the list
        // 1. create the adapter
        // 2. create data source
        // 3. set the adapter on the recycler view
        rvActivities.setAdapter(adapter);
        // 4. set the layout manager on the recycler view
        rvActivities.setLayoutManager(new LinearLayoutManager(getContext()));
        queryActivity();

    }

    private void queryActivity(){
        ParseQuery<Activity> query = ParseQuery.getQuery(Activity.class);
        // Retrieve the most recent ones
        query.orderByDescending("startDate");

        // Only retrieve the last ten
        query.setLimit(10);
        query.include(Activity.KEY_GROUPNOTIFIED);
        query.include(Activity.KEY_AUTHOR);

        query.findInBackground(new FindCallback<Activity>() {
            @Override
            public void done(List<Activity> activities, ParseException e) {
                if (e != null) {
                    Log.e(TAG,"Issue with getting activities", e);
                    return;
                }
                for (Activity activity: activities){
                    Log.i(TAG, "Activity: " + activity.getActivityname());
                }
                allActivities.addAll(activities);
                adapter.notifyDataSetChanged();
            }
        });

    }
}