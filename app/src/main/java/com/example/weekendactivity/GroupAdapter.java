package com.example.weekendactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder>
{
    private Context context;
    private List<Group> groups;

    public GroupAdapter(Context context, List<Group> groups)
    {
        this.context = context;
        this.groups = groups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Group group = groups.get(position);
        holder.bind(group);
    }

    @Override
    public int getItemCount()
    {
        return groups.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvGroupName;
        private TextView tvMembers;
        private TextView tvDescription;
        private ImageView ivGroupImage;

        private String membersText;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            tvMembers = itemView.findViewById(R.id.tvMembers);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivGroupImage = itemView.findViewById(R.id.ivGroupImage);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Group group)
        {
            tvGroupName.setText(group.getGroupname());
            tvDescription.setText(group.getDescription());

            membersText = (group.getMembers().size() == 1) ? group.getMembers().size() + " member" : group.getMembers().size() + " members";

            tvMembers.setText(membersText);

            ParseFile image = group.getGroupImage();
            if (image != null)
            {
                Glide.with(context)
                    .load(image.getUrl())
                    .transform(new RoundedCornersTransformation(30, 10))
                    .circleCrop()
                    .into(ivGroupImage);
            }

        }
    }
}
