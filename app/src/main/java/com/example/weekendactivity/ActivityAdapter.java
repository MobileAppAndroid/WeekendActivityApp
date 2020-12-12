package com.example.weekendactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.security.PrivateKey;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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
        private TextView tvRegCount;
        private TextView tvCommentCount;
        private TextView tvDateTime;
        private TextView tvRelativeTime;
        private TextView tvDayofWeek;
        private TextView tvLocation;
        private TextView tvNewActivity;
        private TextView tvDescription;
        private ImageView ivGroup;
        private ImageView ivLocation;
        private ImageView ivComment;
        private Button btnAddActivity;
        private RelativeLayout containerActivity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvActivityName = itemView.findViewById(R.id.tvActivityName);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
            tvRegCount = itemView.findViewById(R.id.tvRegCount);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvRelativeTime = itemView.findViewById(R.id.tvRelativeTime);
            tvDayofWeek = itemView.findViewById(R.id.tvDayofWeek);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvNewActivity = itemView.findViewById(R.id.tvNewActivity);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivGroup = itemView.findViewById(R.id.ivGroup);
            ivLocation = itemView.findViewById(R.id.ivLocation);
            ivComment = itemView.findViewById(R.id.ivComment);
            btnAddActivity = itemView.findViewById(R.id.btnAddActivity);
            containerActivity = itemView.findViewById(R.id.activityContainer);

        }

        @SuppressLint({"ResourceAsColor", "SetTextI18n"})
        public void bind(final Activity activity) {
            User author = (User) activity.getAuthor();
            final Group groupNotified = activity.getGroupNotified();
            final User currentUser = (User) ParseUser.getCurrentUser();
            final List<String> commentsId = new ArrayList<>();
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

            int radius = 30; // corner radius, higher value = more rounded
            int margin = 10; // crop margin, set to 0 for corners with no crop
            if (image != null) {
                Glide.with(context)
                        .load(activity.getGroupNotified().getGroupImage().getUrl())
                        .transform(new MultiTransformation(new FitCenter(), new RoundedCornersTransformation(radius, margin)))
                        .into(ivGroup); }
            tvRegCount.setText(String.valueOf(activity.getMemberRegistered().size())+"/"+String.valueOf(groupNotified.getMembers().size()));



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

            if (activity.getLocation() != null){
                tvLocation.setText(activity.getLocation());
            }
            else{
                containerActivity.removeView(tvLocation);
                containerActivity.removeView(ivLocation);
            }

            if (activity.getDescription() != null){
                tvDescription.setText(activity.getDescription());
            }
            else{
                containerActivity.removeView(tvDescription);
            }

            if (activity.getComments() != null){
                tvCommentCount.setText(String.valueOf(activity.getComments().size()));
            }else{
                containerActivity.removeView(tvCommentCount);
                containerActivity.removeView(ivComment);
            }


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

            containerActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "show activity detail", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, ActivityDetail.class);
                    intent.putExtra("activity", Parcels.wrap(activity));
                    context.startActivity(intent);

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
}
