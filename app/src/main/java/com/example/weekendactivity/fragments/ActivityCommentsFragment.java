package com.example.weekendactivity.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weekendactivity.Activity;
import com.example.weekendactivity.Comment;
import com.example.weekendactivity.CommentAdapter;
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
 * Use the {@link ActivityCommentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityCommentsFragment extends Fragment {

    private static final String TAG = "CommentsList";

    private RecyclerView rvComments;
    List<Comment> allComments;
    CommentAdapter adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ActivityCommentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActivityCommentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivityCommentsFragment newInstance(String param1, String param2) {
        ActivityCommentsFragment fragment = new ActivityCommentsFragment();
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
        return inflater.inflate(R.layout.fragment_activity_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvComments = view.findViewById(R.id.rvComments);

        allComments = new ArrayList<>();

        adapter = new CommentAdapter(getContext(), allComments);

        // Steps to use the recycler view
        // 0. create layout for one row in the list
        // 1. create the adapter
        // 2. create data source
        // 3. set the adapter on the recycler view
        rvComments.setAdapter(adapter);
        // 4. set the layout manager on the recycler view
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));

        queryComments();
    }

    private void queryComments() {
        final Activity activity = (Activity) Parcels.unwrap(getActivity().getIntent().getParcelableExtra("activity"));
        List<String> commentId = new ArrayList<>();
        commentId = activity.getComments();
        if (commentId != null){
            ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
            // Retrieve the most recent ones
            query.orderByDescending("createdAt");

            // Only retrieve the last ten
            query.setLimit(10);
    //        query.include(Activity.KEY_GROUPNOTIFIED);
            query.include(Comment.KEY_AUTHOR);

            query.whereContainedIn("objectId",commentId);
            query.findInBackground(new FindCallback<Comment>() {
                @Override
                public void done(List<Comment> comments, ParseException e) {
                    if (e != null) {
                        Log.e(TAG,"Issue with getting registered members", e);
                        return;
                    }
                    for (Comment comment: comments){
                        Log.i(TAG, "Commenter: " + comment.getContent());
                    }
                    allComments.addAll(comments);
                    adapter.notifyDataSetChanged();
                }
        });
        }

    }
}