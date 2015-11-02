package com.codepath.instagram.helpers.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramUser;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by araiff on 11/1/15.
 */
public class SearchUserResultsAdapter extends RecyclerView.Adapter<SearchUserResultsAdapter.ViewHolder> {

    private List<InstagramUser> instagramUsers;

    public SearchUserResultsAdapter(List<InstagramUser> users) {
        instagramUsers = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View postView = inflater.inflate(R.layout.layout_item_user, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Context context = viewHolder.sdvProfilePic.getContext();
        InstagramUser user = instagramUsers.get(position);

        // Set item views based on the data model
        // Username
        viewHolder.tvUsername.setText(user.userName);
        // Fullname
        viewHolder.tvFullname.setText(user.fullName);
        // Profile Picture
        viewHolder.sdvProfilePic.setImageURI(Uri.parse(user.profilePictureUrl));
    }

    @Override
    public int getItemCount() {
        return instagramUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvUsername;
        public TextView tvFullname;
        public SimpleDraweeView sdvProfilePic;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvFullname = (TextView) itemView.findViewById(R.id.tvFullname);
            sdvProfilePic = (SimpleDraweeView) itemView.findViewById(R.id.sdvProfilePic);


        }
    }
}
