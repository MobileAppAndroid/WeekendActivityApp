package com.example.weekendactivity.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weekendactivity.Activity;
import com.example.weekendactivity.ActivityDetail;
import com.example.weekendactivity.Comment;
import com.example.weekendactivity.CommentAdapter;
import com.example.weekendactivity.CreateGroupActivity;
import com.example.weekendactivity.MainActivity;
import com.example.weekendactivity.R;
import com.example.weekendactivity.RegMemberAdapter;
import com.example.weekendactivity.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
    private EditText etComment;
    private ImageView ivAddPhoto;
    private Button btnSendComment;

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

        etComment = view.findViewById(R.id.etComment);
        ivAddPhoto = view.findViewById(R.id.ivAddPhoto);
        btnSendComment = view.findViewById(R.id.btnSendComment);

        // Steps to use the recycler view
        // 0. create layout for one row in the list
        // 1. create the adapter
        // 2. create data source
        // 3. set the adapter on the recycler view
        rvComments.setAdapter(adapter);
        // 4. set the layout manager on the recycler view
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));

        queryComments();

        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    btnSendComment.performClick();
                }
                return false;
            }
        });
        final Activity activity = (Activity) Parcels.unwrap(getActivity().getIntent().getParcelableExtra("activity"));
        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etComment.getText().toString().length() > 0) {
                    final Comment newComment = new Comment();
                    newComment.setAuthor(ParseUser.getCurrentUser());
                    newComment.setContent(etComment.getText().toString());
                    newComment.setKeyActivity(activity);
                    allComments.add(newComment);
                    adapter.notifyItemInserted(0);
                    newComment.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null)
                            {
                                Log.e(TAG, "Issue with creating a comment", e);
                                Toast.makeText(getContext(), "Failed to create comment", Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                String commentId;
                                commentId = newComment.getObjectId();
                                Toast.makeText(getContext(), "Comment sent successfully", Toast.LENGTH_SHORT).show();
                                Log.i(TAG,"newComment objectId: "+newComment.getObjectId());
                                Toast.makeText(getContext(),"newComment objectId: "+commentId, Toast.LENGTH_SHORT).show();
                                updateActivityCommentsList(commentId);
//                                allComments.add(newComment);
//                                adapter.notifyItemInserted(0);
                                queryComments();
                            }

                        }
                    });
                    etComment.getText().clear();

//                    activity.addToComments(newComment);
//                    activity.saveInBackground();
//                    queryComments();
                }

            }
        });
    }

    private void updateActivityCommentsList(final String commentId) {
        final Activity activity = (Activity) Parcels.unwrap(getActivity().getIntent().getParcelableExtra("activity"));
        activity.fetchInBackground(new GetCallback<Activity>() {
            public void done(Activity object, ParseException e) {
                if (e == null) {
                    // Success!
                    Log.i(TAG,"Successfully get the newest activity. new Length of activity comments: "+activity.getComments().size());
                } else {
                    // Failure!
                    Log.e(TAG,"Issue with getting newest activity", e);
                    return;
                }
            }
        });
        ParseQuery<Activity> query = ParseQuery.getQuery(Activity.class);
        query.whereEqualTo("objectId",activity.getObjectId());

// Retrieve the object by id
        query.getInBackground(activity.getObjectId().toString(), new GetCallback<Activity>() {
            public void done(Activity activityToUpdate, ParseException e) {
                if (e != null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to your Parse Server. playerName hasn't changed.
                    Log.e(TAG,"Issue with updating activity comments, objectId: " + activity.getObjectId(), e);
                    return;

                }
                else{
                    activityToUpdate.addToComments(commentId);
                    Log.i(TAG,"Success in updating activity comments: "+activity.getComments().size());

                    activityToUpdate.saveInBackground();
                }
            }
        });
    }


    private void queryComments() {
        final Activity activity = (Activity) Parcels.unwrap(getActivity().getIntent().getParcelableExtra("activity"));
        activity.fetchInBackground(new GetCallback<Activity>() {
            public void done(Activity object, ParseException e) {
                if (e == null) {
                    // Success!
                    Log.i(TAG,"Successfully get the newest activity.");
                } else {
                    // Failure!
                    Log.e(TAG,"Issue with getting newest activity", e);
                    return;
                }
            }
        });
        List<String> commentIds = new ArrayList<>();
        commentIds = activity.getComments();
        if (commentIds != null){
            ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
            // Retrieve the most recent ones
            query.orderByDescending("createdAt");

            // Only retrieve the last ten
            query.setLimit(10);
    //        query.include(Activity.KEY_GROUPNOTIFIED);
            query.include(Comment.KEY_AUTHOR);

            query.whereContainedIn("objectId",commentIds);
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
                    allComments.clear();
                    allComments.addAll(comments);
                    adapter.notifyDataSetChanged();
                }
        });
        }

    }
    public void onResume()
    {
        super.onResume();

        // Set title bar
        ((ActivityDetail) getActivity()).setActionBarTitle("Comment");
    }
}