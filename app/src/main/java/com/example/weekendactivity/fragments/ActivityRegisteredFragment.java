package com.example.weekendactivity.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weekendactivity.Activity;
import com.example.weekendactivity.R;
import com.example.weekendactivity.RegMemberAdapter;
import com.example.weekendactivity.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static com.example.weekendactivity.fragments.ActivityRegisteredFragment.Utility.calculateNoOfColumns;
import static com.parse.Parse.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivityRegisteredFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityRegisteredFragment extends Fragment {

    private static final String TAG = "RegisteredMembersList";

    private RecyclerView rvRegisteredMembers;
    List<User> allMembersRegistered;
    RegMemberAdapter adapter;
    private int numColumns;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ActivityRegisteredFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActivityRegisteredFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivityRegisteredFragment newInstance(String param1, String param2) {
        ActivityRegisteredFragment fragment = new ActivityRegisteredFragment();
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
        return inflater.inflate(R.layout.fragment_activity_registered, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvRegisteredMembers = view.findViewById(R.id.rvRegisteredMembers);

        allMembersRegistered = new ArrayList<>();

        adapter = new RegMemberAdapter(getContext(), allMembersRegistered);

        numColumns =  calculateNoOfColumns(getApplicationContext(), 80);

        // Steps to use the recycler view
        // 0. create layout for one row in the list
        // 1. create the adapter
        // 2. create data source
        // 3. set the adapter on the recycler view
        rvRegisteredMembers.setAdapter(adapter);
        // 4. set the layout manager on the recycler view
        rvRegisteredMembers.setLayoutManager(new GridLayoutManager(getContext(),numColumns));



        queryMembersRegistered();
    }

    private void queryMembersRegistered() {

        final Activity activity = (Activity) Parcels.unwrap(getActivity().getIntent().getParcelableExtra("activity"));
        List<String> regMemberId = new ArrayList<>();
        regMemberId = activity.getMemberRegistered();
        if(regMemberId !=null ){
            ParseQuery<User> query = ParseQuery.getQuery(User.class);
            // Retrieve the most recent ones
            query.orderByDescending("username");

            // Only retrieve the last ten
            query.setLimit(10);
//        query.include(Activity.KEY_GROUPNOTIFIED);
//        query.include(Activity.KEY_AUTHOR);

            query.whereContainedIn("objectId",regMemberId);
            query.findInBackground(new FindCallback<User>() {
                @Override
                public void done(List<User> members, ParseException e) {
                    if (e != null) {
                        Log.e(TAG,"Issue with getting registered members", e);
                        return;
                    }
                    for (User member: members){
                        Log.i(TAG, "Registered Member: " + member.getUsername());
                    }
                    allMembersRegistered.addAll(members);
                    adapter.notifyDataSetChanged();
                }
            });
        }

    }

    public static class Utility {
        public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
            int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
            return noOfColumns;
        }
    }
}