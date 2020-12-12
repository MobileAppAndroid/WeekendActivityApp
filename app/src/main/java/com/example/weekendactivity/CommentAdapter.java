package com.example.weekendactivity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;
    private List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments){
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() { return comments.size(); }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvScreenname;
        private TextView tvContent;
        private TextView tvRelativeTime;
        private TextView tvDatetime;
        private ImageView ivUserProfile;
        private ImageView ivActivityImage;
        private RelativeLayout commentContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvScreenname = itemView.findViewById(R.id.tvScreenname);
            tvContent = itemView.findViewById(R.id.tvComment);
            tvRelativeTime = itemView.findViewById(R.id.tvRelativeTime);
            tvDatetime = itemView.findViewById(R.id.tvDatetime);
            ivUserProfile = itemView.findViewById(R.id.ivUserProfile);
            ivActivityImage = itemView.findViewById(R.id.ivActivityImage);

            commentContainer = itemView.findViewById(R.id.rlCommentContainer);

        }

        public void bind(Comment comment) {

            User commentAuthor = (User) comment.getAuthor();
            final User currentUser = (User) ParseUser.getCurrentUser();
            Date createdAt = comment.getCreatedAt();
            DateFormat dateFormat = new SimpleDateFormat("MMM d h:mm aaa", Locale.ENGLISH);

            String authorLabel;
            if (commentAuthor.getObjectId().equals(currentUser.getObjectId())){
                authorLabel = " (Me) ";
                tvScreenname.setTextColor(Color.RED);
            } else{
                authorLabel = " ";
                tvScreenname.setTextColor(Color.GRAY);
            }

            if (commentAuthor.getScreenname() != null) {
                tvScreenname.setText(commentAuthor.getScreenname()+authorLabel);
            } else{
                tvScreenname.setText(commentAuthor.getUsername()+authorLabel);
            }

            if(comment.getContent() !=null ){
                tvContent.setText(comment.getContent());
            }
            else{
                commentContainer.removeView(tvContent);
            }

            tvDatetime.setText("\u2022" + " "+dateFormat.format(createdAt));
            tvRelativeTime.setText(getTimeDifference(createdAt));

            ParseFile image = commentAuthor.getProfileImage();
            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .transform(new MultiTransformation(new FitCenter(), new RoundedCorners(10)))
                        .into(ivUserProfile); }
            else{
                Glide.with(context)
                        .load(context.getResources().getIdentifier(String.valueOf(R.drawable.ic_baseline_person),"drawlable",context.getPackageName()))
                        .transform(new MultiTransformation(new FitCenter(), new RoundedCorners(10)))
                        .into(ivUserProfile);
            }

            ParseFile activityImage = comment.getImage();
            if (activityImage != null) {
                Glide.with(context)
                        .load(activityImage.getUrl())
                        .transform(new MultiTransformation(new FitCenter(), new RoundedCorners(10)))
                        .into(ivActivityImage); }
            else{
                commentContainer.removeView(ivActivityImage);
            }
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
