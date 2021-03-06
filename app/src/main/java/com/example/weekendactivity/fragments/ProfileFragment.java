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

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileFragment extends Fragment {

    private TextView tvUsername;
    private TextView tvScreenName;
    private Button btnLogout;
    private ImageView ivProfile;
    private Button btnFriends;

    private User currentUser;
    private List<String> friendsList;

    public ProfileFragment() {
        // Required empty public constructor
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
        tvScreenName = view.findViewById(R.id.tvScreenName);
        btnLogout = view.findViewById(R.id.btnLogout);
        ivProfile = view.findViewById(R.id.ivProfile);

        btnFriends = view.findViewById(R.id.btnFriends);

        currentUser = (User) ParseUser.getCurrentUser();
        friendsList = currentUser.getFriends();

        tvUsername.setText("@" + ParseUser.getCurrentUser().getUsername());

        String accountName = "";

        if (ParseUser.getCurrentUser().getString("screenName") != null){
            accountName = ParseUser.getCurrentUser().getString("screenName");
        } else{
            accountName = ParseUser.getCurrentUser().getUsername();
        }
        tvScreenName.setText(accountName);

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

        int numFriends = friendsList.size();
        String strNumFriends = String.valueOf(numFriends);
        btnFriends.setText((numFriends == 1) ? strNumFriends + " Friend" : strNumFriends + " Friends");

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