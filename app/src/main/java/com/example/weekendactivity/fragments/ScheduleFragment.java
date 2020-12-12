package com.example.weekendactivity.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weekendactivity.R;

import java.util.Random;


public class ScheduleFragment extends Fragment {

//    private static final int TYPE_DAY_VIEW = 1;
//    private static final int TYPE_THREE_DAY_VIEW = 2;
//    private static final int TYPE_WEEK_VIEW = 3;
//    private static final Random random = new Random();
//    protected WeekView mWeekView;
//    private int mWeekViewType = TYPE_THREE_DAY_VIEW;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        TextView draggableView = view.findViewById(R.id.draggable_view);
//        // draggableView.setOnLongClickListener(new DragTapListener());
//
//        // Get a reference for the week view in the layout.
//        mWeekView = view.findViewById(R.id.weekView);
//
//        // Show a toast message about the touched event.
//        mWeekView.setOnEventClickListener((WeekView.EventClickListener) this);
//
//        // The week view has infinite scrolling horizontally. We have to provide the events of a
//        // month every time the month changes on the week view.
//        mWeekView.setWeekViewLoader((WeekView.WeekViewLoader) this);
//
//        // Set long press listener for events.
//        mWeekView.setEventLongPressListener((WeekView.EventLongPressListener) this);
//
//        // Set long press listener for empty view
//        mWeekView.setEmptyViewLongPressListener((WeekView.EmptyViewLongPressListener) this);
//
//        // Set EmptyView Click Listener
//        mWeekView.setEmptyViewClickListener((WeekView.EmptyViewClickListener) this);
//
//        // Set AddEvent Click Listener
//        mWeekView.setAddEventClickListener((WeekView.AddEventClickListener) this);
//
//        // Set Drag and Drop Listener
//        mWeekView.setDropListener((WeekView.DropListener) this);
//
//        //mWeekView.setAutoLimitTime(true);
//        //mWeekView.setLimitTime(4, 16);
//
//        //mWeekView.setMinTime(10);
//        //mWeekView.setMaxTime(20);
//
//        // Set up a date time interpreter to interpret how the date and time will be formatted in
//        // the week view. This is optional.
//       //  setupDateTimeInterpreter();
    }
}