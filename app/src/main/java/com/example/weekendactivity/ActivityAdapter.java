package com.example.weekendactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.security.PrivateKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.weekendactivity.R.color.colorPrimary;
import static com.example.weekendactivity.R.color.colorPrimaryDark;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private Context context;
    private List<Activity> activities;

    public ActivityAdapter(Context context, List<Activity> activities) {
        this.context = context;
        this.activities = activities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_activity,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Activity activity = activities.get(position);
        holder.bind(activity);
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvGroupName;
        private TextView tvActivityName;
        private TextView tvAuthorName;
        private ImageView ivGroup;
        private TextView tvRegCount;
        private TextView tvDateTime;
        private TextView tvRelativeTime;
        private TextView tvLocation;
        private TextView tvDescription;
        private Button btnAddActivity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvActivityName = itemView.findViewById(R.id.tvActivityName);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
            ivGroup = itemView.findViewById(R.id.ivGroup);
            tvRegCount = itemView.findViewById(R.id.tvRegCount);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvRelativeTime = itemView.findViewById(R.id.tvRelativeTime);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnAddActivity = itemView.findViewById(R.id.btnAddActivity);

        }

        @SuppressLint("ResourceAsColor")
        public void bind(final Activity activity) {
            User author = (User) activity.getAuthor();
            final Group groupNotified = activity.getGroupNotified();
            final User currentUser = (User) ParseUser.getCurrentUser();
            String authorLabel;
            if (author.getObjectId()==currentUser.getObjectId()){
                authorLabel = "(Me) @";
                tvAuthorName.setTextColor(Color.RED);
            } else{
                authorLabel = " @";
                tvAuthorName.setTextColor(Color.GRAY);
            }

            tvActivityName.setText(activity.getActivityname());
            tvGroupName.setText(activity.getGroupNotified().getString("groupName"));
            if (author.getScreenname() != null) {
                tvAuthorName.setText(author.getScreenname()+authorLabel);
            } else{
                tvAuthorName.setText(author.getUsername()+authorLabel);
            }
            ParseFile image = activity.getGroupNotified().getGroupImage();
            if (image != null) {
                Glide.with(context)
                        .load(activity.getGroupNotified().getGroupImage().getUrl())
                        .transform(new MultiTransformation(new FitCenter(), new RoundedCorners(10)))
                        .into(ivGroup); }
            tvRegCount.setText(String.valueOf(activity.getMemberRegistered().size())+"/"+String.valueOf(groupNotified.getMembers().size()));

            Date startDate = activity.getStartDate();
            // Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
            String strDate = dateFormat.format(startDate);
            if (startDate != null){
                tvDateTime.setText(strDate);
            }
            tvLocation.setText(activity.getLocation());
            tvDescription.setText(activity.getDescription());

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
    }
}
