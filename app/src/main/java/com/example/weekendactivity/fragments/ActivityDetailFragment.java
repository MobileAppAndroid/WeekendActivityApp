package com.example.weekendactivity.fragments;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.weekendactivity.Activity;
import com.example.weekendactivity.ActivityDetail;
import com.example.weekendactivity.Group;
import com.example.weekendactivity.R;
import com.example.weekendactivity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Intent.getIntent;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivityDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityDetailFragment extends Fragment {

    private TextView tvGroupName;
    private TextView tvActivityName;
    private TextView tvAuthorName;
    private ImageView ivGroup;
    private ImageView ivComment;
    private TextView tvRegCount;
    private TextView tvDateTime;
    private TextView tvRelativeTime;
    private TextView tvDayofWeek;
    private TextView tvLocation;
    private TextView tvNewActivity;
    private TextView tvDescription;
    private TextView tvCommentCount;
    private Button btnAddActivity;
    private RelativeLayout containerActivity;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ActivityDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActivityDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivityDetailFragment newInstance(String param1, String param2) {
        ActivityDetailFragment fragment = new ActivityDetailFragment();
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
        return inflater.inflate(R.layout.fragment_activity_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvActivityName = view.findViewById(R.id.tvActivityName);
        tvGroupName = view.findViewById(R.id.tvGroupName);
        tvAuthorName = view.findViewById(R.id.tvAuthorName);
        ivGroup = view.findViewById(R.id.ivGroup);
        tvRegCount = view.findViewById(R.id.tvRegCount);
        tvDateTime = view.findViewById(R.id.tvDateTime);
        tvRelativeTime = view.findViewById(R.id.tvRelativeTime);
        tvDayofWeek = view.findViewById(R.id.tvDayofWeek);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvNewActivity = view.findViewById(R.id.tvNewActivity);
        tvCommentCount = view.findViewById(R.id.tvCommentCount);
        tvDescription = view.findViewById(R.id.tvDescription);
        btnAddActivity = view.findViewById(R.id.btnAddActivity);
        containerActivity = view.findViewById(R.id.activityContainer);

        final Activity activity = (Activity) Parcels.unwrap(getActivity().getIntent().getParcelableExtra("activity"));

        User author = (User) activity.getAuthor();
        final Group groupNotified = activity.getGroupNotified();
        final User currentUser = (User) ParseUser.getCurrentUser();
        String authorLabel;
        if (author.getObjectId().equals(currentUser.getObjectId())){
            authorLabel = " (Me) @";
            tvAuthorName.setTextColor(Color.RED);
        } else{
            authorLabel = " @";
            tvAuthorName.setTextColor(Color.GRAY);
        }

        tvActivityName.setText(activity.getActivityname()+" "+"\u2022" + " ");
        tvGroupName.setText(activity.getGroupNotified().getString("groupName"));
        if (author.getScreenname() != null) {
            tvAuthorName.setText(author.getScreenname()+authorLabel);
        } else{
            tvAuthorName.setText(author.getUsername()+authorLabel);
        }
        ParseFile image = activity.getGroupNotified().getGroupImage();
        if (image != null) {
            Glide.with(getContext())
                    .load(activity.getGroupNotified().getGroupImage().getUrl())
                    .transform(new MultiTransformation(new FitCenter(), new RoundedCorners(10)))
                    .into(ivGroup); }
        if(activity.getMemberRegistered() != null){
            tvRegCount.setText(String.valueOf(activity.getMemberRegistered().size())+"/"+String.valueOf(groupNotified.getMembers().size()));
        }
        if(activity.getComments() != null){
            tvCommentCount.setText(String.valueOf(activity.getComments().size()));
        }else{
//            tvCommentCount.setVisibility(View.INVISIBLE);
            containerActivity.removeView(tvCommentCount);
            containerActivity.removeView(ivComment);
        }



        Date startDate = activity.getStartDate();
        Date endDate = activity.getEndDate();
        Date currentDate = Calendar.getInstance().getTime();
        // Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("MMM d h:mm aaa", Locale.ENGLISH); //new SimpleDateFormat("yyyy-MM-dd hh:mm");
        DateFormat dayofWeekFormat = new SimpleDateFormat("EEE", Locale.ENGLISH);
        DateFormat clockFormat = new SimpleDateFormat("h:mm aaa",Locale.ENGLISH);

        String strStartDate = dateFormat.format(startDate);
        String strEndDate = dateFormat.format(endDate);
        String weekDay = dayofWeekFormat.format(startDate);
        String strEndClock = clockFormat.format(endDate);

        if (startDate != null){
            String relativeTime = getTimeDifference(startDate);
            tvRelativeTime.setText(relativeTime);
            if (relativeTime.endsWith("ago")){
                tvRelativeTime.setTextColor(Color.GRAY);
            }

            long timediff= (System.currentTimeMillis() - activity.getCreatedAt().getTime()) / 1000;
            if ( timediff < 5 ){
                tvNewActivity.setText("new");
                tvNewActivity.setTextColor(Color.RED);
            }

            tvDayofWeek.setText(weekDay);
            if(endDate != null) {
                tvDateTime.setText(strStartDate + "-" + strEndClock);
            }
            else{
                tvDateTime.setText(strStartDate);
            }
        }
        else{
            tvDateTime.setText("TBD");
        }

        tvLocation.setText(activity.getLocation());
        tvDescription.setText(activity.getDescription());

        if (currentDate.after(startDate)){
            //btnAddActivity.setVisibility(View.INVISIBLE);
            if (activity.IsRegistered(currentUser)){
                btnAddActivity.setText("ATTENDED");
            }else{
                btnAddActivity.setText("PAST");
            }
            btnAddActivity.setBackgroundColor(Color.LTGRAY);
            btnAddActivity.setClickable(false);
        }else{
            if (activity.IsRegistered(currentUser)) {
                btnAddActivity.setText("ADDED");
                btnAddActivity.setBackgroundColor(Color.GREEN);

            }else{
                btnAddActivity.setText("ADD");
                btnAddActivity.setBackgroundColor(Color.RED);
            }

            btnAddActivity.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    if (activity.IsRegistered(currentUser)){
                        if (currentUser != activity.getAuthor()) {
                            activity.deleteFromMemberregistered(currentUser);
                            btnAddActivity.setText("ADD");
                            btnAddActivity.setBackgroundColor(Color.RED);
                            tvRegCount.setText(String.valueOf(activity.getMemberRegistered().size()) + "/" + String.valueOf(groupNotified.getMembers().size()));
                            activity.saveInBackground();
                        }
                    }
                    else{
                        activity.addToMemberregistered(currentUser);
                        btnAddActivity.setText("ADDED");
                        btnAddActivity.setBackgroundColor(Color.GREEN);
                        tvRegCount.setText(String.valueOf(activity.getMemberRegistered().size())+"/"+String.valueOf(groupNotified.getMembers().size()));
                        activity.saveInBackground();
                    }
                }
            });
        }

        tvCommentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "show activity detail", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getContext(), ActivityDetail.class);
//                intent.putExtra("activity", Parcels.wrap(activity));
//                getContext().startActivity(intent);
//                FragmentTransaction t = getActivity().getFragmentManager().beginTransaction();
//                Fragment mFrag = new ActivityCommentsFragment();
//                t.replace(R.id.flContainer, mFrag);
//                t.commit();

                Fragment fragment = new ActivityCommentsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                ((BottomNavigationView)getActivity().findViewById(R.id.bottomNavigation)).setSelectedItemId(R.id.action_comment);

            }
        });

        tvRegCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "show activity detail", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getContext(), ActivityDetail.class);
//                intent.putExtra("activity", Parcels.wrap(activity));
//                getContext().startActivity(intent);
//                FragmentTransaction t = getActivity().getFragmentManager().beginTransaction();
//                Fragment mFrag = new ActivityCommentsFragment();
//                t.replace(R.id.flContainer, mFrag);
//                t.commit();

                Fragment fragment = new ActivityRegisteredFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                ((BottomNavigationView)getActivity().findViewById(R.id.bottomNavigation)).setSelectedItemId(R.id.action_registeredMembers);


            }
        });



    }
    public String getTimeDifference(Date date) {
        String time = "";
        String timeSuffix = "";
        long diff = (System.currentTimeMillis() - date.getTime()) / 1000;
        if (diff < 0) {
            timeSuffix = " to go";
            diff = -diff;
        }
        else{
            timeSuffix = " ago";
        }

        //if (diff < 5)
        //    time = "Just now";
        //else
        if (diff < 60)
            time = String.format(Locale.ENGLISH, "%ds",diff);
        else if (diff < 60 * 60)
            time = String.format(Locale.ENGLISH, "%dm", diff / 60);
        else if (diff < 60 * 60 * 24)
            time = String.format(Locale.ENGLISH, "%dh", diff / (60 * 60));
        else if (diff < 60 * 60 * 24 * 30)
            time = String.format(Locale.ENGLISH, "%dd", diff / (60 * 60 * 24));
        else {
            Calendar now = Calendar.getInstance();
            Calendar then = Calendar.getInstance();
            then.setTime(date);
            if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR)) {
                time = String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + " "
                        + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);
            } else {
                time = String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + " "
                        + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)
                        + " " + String.valueOf(then.get(Calendar.YEAR) - 2000);
            }
        }
        return time + timeSuffix;
    }
}