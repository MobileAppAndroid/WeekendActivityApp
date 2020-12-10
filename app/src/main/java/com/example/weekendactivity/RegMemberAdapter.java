package com.example.weekendactivity;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseFile;

import java.util.List;

public class RegMemberAdapter extends RecyclerView.Adapter<RegMemberAdapter.ViewHolder> {

    private Context context;
    private List<User> membersRegistered;

    public RegMemberAdapter(Context context, List<User> users){
        this.context = context;
        this.membersRegistered = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User member = membersRegistered.get(position);
        holder.bind(member);
    }

    @Override
    public int getItemCount() { return membersRegistered.size(); }


    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivUserProfile;
        private TextView tvScreenname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvScreenname = itemView.findViewById(R.id.tvScreenname);
            ivUserProfile = itemView.findViewById(R.id.ivUserProfile);
        }

        public void bind(User member) {
            if (member.getScreenname() != null) {
                tvScreenname.setText(member.getScreenname());
            } else{
                tvScreenname.setText(member.getUsername());
            }
            ParseFile image = member.getProfileImage();
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
        }

    }

}
