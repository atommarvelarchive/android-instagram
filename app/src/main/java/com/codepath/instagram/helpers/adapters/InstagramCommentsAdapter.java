package com.codepath.instagram.helpers.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.UIUtils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramUser;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by araiff on 10/29/15.
 */
public class InstagramCommentsAdapter extends RecyclerView.Adapter<InstagramCommentsAdapter.ViewHolder> {

    private List<InstagramComment> instagramComments;

    public InstagramCommentsAdapter(List<InstagramComment> comments) {
        instagramComments = comments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View commentView = inflater.inflate(R.layout.layout_item_comment, parent, false);

        //Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(commentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Context context = viewHolder.sdvProfilePic.getContext();
        InstagramComment comment = instagramComments.get(position);
        InstagramUser user = comment.user;

        // Set item views based on the data model
        // Username + Comment
        viewHolder.tvComment.setText(UIUtils.getCommentSpan(context, user.userName, comment.text));
        // TimeStamp
        viewHolder.tvTimeStamp.setText(DateUtils.getRelativeTimeSpanString(comment.createdTime * 1000));
        // Profile Picture
        viewHolder.sdvProfilePic.setImageURI(Uri.parse(user.profilePictureUrl));
    }

    @Override
    public int getItemCount() {
        return instagramComments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvComment;
        public TextView tvTimeStamp;
        public SimpleDraweeView sdvProfilePic;

        public ViewHolder(View itemView) {
            super(itemView);

            tvComment = (TextView) itemView.findViewById(R.id.tvComment);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
            sdvProfilePic = (SimpleDraweeView) itemView.findViewById(R.id.sdvProfilePic);
        }
    }
}
