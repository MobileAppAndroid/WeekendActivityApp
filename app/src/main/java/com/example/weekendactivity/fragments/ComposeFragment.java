package com.example.weekendactivity.fragments;

import com.example.weekendactivity.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.weekendactivity.Group;
import com.example.weekendactivity.MainActivity;
import com.example.weekendactivity.R;
import com.example.weekendactivity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.widget.CompoundButton.*;
import static java.util.Calendar.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "ComposeFragment";
    private EditText etEventName;
    private EditText etDuration;
    private EditText etLocation;
    private EditText etMinParticipants;
    private EditText etMaxParticipants;
    private EditText etDescription;
    private Spinner spinnerActivityCards;
    private Spinner spinnerGroupToNotify;
    private DatePicker dpDateInMonth;
    private CheckBox cbSaSu;
    private CheckBox cbWeekdays;
    private CheckBox cbMorning;
    private CheckBox cbAfternoon;
    private CheckBox cbNight;
    private CheckBox cbSetUpPoll;
    private CheckBox cbAddToCards;
    private RadioButton radioRepeatEachWeek;
    private RadioButton radioRepeatEachMonth;
    private RadioButton radioNoRepeat;
    private Button btnSync;
    private Button btnAddTime;
    private Button btnAddLocation;
    private Button btnCancel;
    private Button btnCreateActivity;

    private List<Group> allGroups;
    private Activity newActivity;
    private User author;
    private Group groupToNotify;
    private int groupSelectedInd;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ComposeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComposeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComposeFragment newInstance(String param1, String param2) {
        ComposeFragment fragment = new ComposeFragment();
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
        return inflater.inflate(R.layout.fragment_compose, container, false);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        hideKeyboardFrom(getContext(), view);
//        setupUI(view.findViewById(R.id.parent));
        // Get a reference for the week view in the layout.
    //        mWeekView = (WeekView) findViewById(R.id.weekView);
//
//// Set an action when any event is clicked.
//        mWeekView.setOnEventClickListener(mEventClickListener);
//
//// The week view has infinite scrolling horizontally. We have to provide the events of a
//// month every time the month changes on the week view.
//        mWeekView.setMonthChangeListener(mMonthChangeListener);
//
//// Set long press listener for events.
//        mWeekView.setEventLongPressListener(mEventLongPressListener);

        etEventName = view.findViewById(R.id.etEventName);
        etDuration = view.findViewById(R.id.etDuration);
        etLocation = view.findViewById(R.id.etLocation);
        etMinParticipants = view.findViewById(R.id.etMinParticipants);
        etMaxParticipants = view.findViewById(R.id.etMaxParticipants);
        etDescription = view.findViewById(R.id.etDescription);
        spinnerActivityCards = view.findViewById(R.id.spinnerActivtyCards);
        spinnerGroupToNotify = view.findViewById(R.id.spinnerGroupToNotify);
        dpDateInMonth = view.findViewById(R.id.dpDateInMonth);
        cbSaSu = view.findViewById(R.id.cbConstraintSaSu);
        cbWeekdays = view.findViewById(R.id.cbConstraintWeekdays);
        cbMorning = view.findViewById(R.id.cbConstraintMorning);
        cbAfternoon = view.findViewById(R.id.cbConstraintAfternoon);
        cbNight = view.findViewById(R.id.cbConstraintNight);
        cbSetUpPoll = view.findViewById(R.id.cbSetUpPoll);
        cbAddToCards = view.findViewById(R.id.cbAddToCards);
        radioRepeatEachWeek = view.findViewById(R.id.radio_eachWeek);
        radioRepeatEachMonth = view.findViewById(R.id.radio_eachMonth);
        radioNoRepeat = view.findViewById(R.id.radio_noRepeat);
        btnSync = view.findViewById(R.id.btnSync);
        btnAddTime = view.findViewById(R.id.btnAddTimeOption);
        btnAddLocation = view.findViewById(R.id.btnAddLocationOption);
        btnCancel = view.findViewById(R.id.btnCancelCreate);
        btnCreateActivity = view.findViewById(R.id.btnCreateActivity);

        allGroups = new ArrayList<>();

        newActivity = new Activity();
        author = (User) ParseUser.getCurrentUser();
        newActivity.setActivityname(String.valueOf(etEventName.getText()));

        List<User> membersRegistered = new ArrayList<>();
        membersRegistered.add(author);
        newActivity.setAuthor(author);
        newActivity.setMemberRegistered(membersRegistered);
        newActivity.setDescription(String.valueOf(etDescription.getText()));
        newActivity.setLocation(etLocation.getText().toString());
        newActivity.setActivityStatus("Scheduled: Date/Location Determined");

        DateFormat dateFormat = new SimpleDateFormat("MMM d h:mm aaa", Locale.ENGLISH);


        Date currentTime = Calendar.getInstance().getTime();
        newActivity.setStartdate(currentTime);
        newActivity.setEnddate(currentTime);
        if (author.getGroups()== null){
            Toast.makeText(getContext(),TAG + "You need to create a group first",Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> groupIds = author.getGroups();
        queryGroups(groupIds);

        // Spinner click listener
        spinnerGroupToNotify.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> groupsOwned = new ArrayList<String>();
        for (Group group : allGroups) {
            groupsOwned.add(group.getGroupname());
        }


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, groupsOwned);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerGroupToNotify.setAdapter(dataAdapter);

        dpDateInMonth.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.flContainer, new ActivityFragment(), "TAG: ActivityFragment");
                ft.commit();
                
                ((BottomNavigationView)getActivity().findViewById(R.id.bottomNavigation)).setSelectedItemId(R.id.action_activity);
            }
        });

        btnCreateActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivity.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e!= null){
                            Log.e(TAG,"failure in creating new activity",e);
                            Toast.makeText(getContext(),TAG + "Failure in creating a new activity",Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            Log.e(TAG,"success in creating new activity",e);
                            Toast.makeText(getContext(),TAG + "Success in creating a new activity",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        cbSetUpPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                    System.out.println("Checked");
                    Toast.makeText(getContext(), "Checked Set Up Poll",Toast.LENGTH_SHORT).show();
                    newActivity.setActivityStatus("On vote");
                } else {
                    System.out.println("Un-Checked");
                    Toast.makeText(getContext(), "Unchecked Set Up Poll",Toast.LENGTH_SHORT).show();
                    newActivity.setActivityStatus("Scheduled: Date/Location Determined");
                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        groupSelectedInd = position;
        groupToNotify = allGroups.get(groupSelectedInd);
        newActivity.setGroupnotified(groupToNotify);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void queryGroups(List<String> groupIds)
    {
        ParseQuery<Group> query = ParseQuery.getQuery(Group.class);
        query.orderByAscending("groupName");
        query.whereContainedIn("objectId ", groupIds);

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
            }
        });
    }

//    public static void hideKeyboardFrom(Context context, View view) {
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }
//
//    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager =
//                (InputMethodManager) activity.getSystemService(
//                        Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(
//                activity.getCurrentFocus().getWindowToken(), 0);
//    }
//
//    public void setupUI(View view) {
//
//        // Set up touch listener for non-text box views to hide keyboard.
//        if (!(view instanceof EditText)) {
//            view.setOnTouchListener(new View.OnTouchListener() {
//                public boolean onTouch(View v, MotionEvent event) {
//                    hideKeyboardFrom(getContext(), getActivity().getCurrentFocus());
////                    hideSoftKeyboard(MyActivity.this);
//                    return false;
//                }
//            });
//        }
//
//        //If a layout container, iterate over children and seed recursion.
//        if (view instanceof ViewGroup) {
//            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//                View innerView = ((ViewGroup) view).getChildAt(i);
//                setupUI(innerView);
//            }
//        }
//    }
    public void onResume()
    {
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity()).setActionBarTitle("Create Activity");
    }
}