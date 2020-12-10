package com.example.weekendactivity.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weekendactivity.AddFriendsActivity;
import com.example.weekendactivity.CreateGroupActivity;
import com.example.weekendactivity.LoginActivity;
import com.example.weekendactivity.MainActivity;
import com.example.weekendactivity.R;
import com.example.weekendactivity.User;
import com.parse.ParseUser;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView tvUsername;
    private TextView tvScreenname;
    private Button btnLogout;
    private ImageView ivProfile;
    private Button btnFriends;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUsername = view.findViewById(R.id.tvUsername);
        btnLogout = view.findViewById(R.id.btnLogout);
        ivProfile = view.findViewById(R.id.ivProfile);

        btnFriends = view.findViewById(R.id.btnFriends);

        tvUsername.setText("@" + ParseUser.getCurrentUser().getUsername());

        String accountName = ParseUser.getCurrentUser().getUsername()+"@";


        if (ParseUser.getCurrentUser().getString("screenName") != null){
            accountName = accountName + ParseUser.getCurrentUser().getString("screenName");
        } else{
            accountName = accountName + ParseUser.getCurrentUser().getUsername();
        }
        tvUsername.setText(accountName);
        int radius = 30; // corner radius, higher value = more rounded
        int margin = 10; // crop margin, set to 0 for corners with no crop

        if (ParseUser.getCurrentUser().getParseFile("profileImage") != null ) {
            Toast.makeText(getContext(),"set the profileimage",Toast.LENGTH_SHORT).show();
            Glide.with(getContext())
                    .load(ParseUser.getCurrentUser().getParseFile("profileImage").getUrl())
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .circleCrop()
                    .into(ivProfile);
        }
        else
        {
            Toast.makeText(getContext(),"use the profileimage",Toast.LENGTH_SHORT).show();
            Glide.with(getContext())
                    .load("http://via.placeholder.com/300.png")
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .circleCrop()
                    .into(ivProfile);
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                goLogin();
            }
        });


        btnFriends.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), AddFriendsActivity.class);
                startActivity(i);
            }
        });

        btnFriends.setText("(#) Friends");

    }

    private void goLogin() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
        return;
    }

    public void onResume()
    {
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity()).setActionBarTitle("Profile");
    }
}