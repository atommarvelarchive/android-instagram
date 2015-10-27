package com.codepath.instagram.helpers;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramImage;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramUser;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by araiff on 10/26/15.
 */
public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.ViewHolder>{

    private List<InstagramPost> instagramPosts;

    public InstagramPostsAdapter(List<InstagramPost> instagramPosts) {
        this.instagramPosts = instagramPosts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.layout_item_post, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Context context = viewHolder.sdvProfile.getContext();
        InstagramPost post = instagramPosts.get(position);
        InstagramUser user = post.user;
        InstagramImage image = post.image;

        // Set item views based on the data model
        // Username
        viewHolder.tvUsername.setText(user.userName);
        // Created Time
        viewHolder.tvCreatedTime.setText(DateUtils.getRelativeTimeSpanString(post.createdTime * 1000));
        // Profile picture
        viewHolder.sdvProfile.setImageURI(Uri.parse(user.profilePictureUrl));
        // Picture
        viewHolder.sdvPic.setImageURI(Uri.parse(image.imageUrl));
        viewHolder.sdvPic.setAspectRatio(image.imageWidth / image.imageHeight);
        // Likes
        viewHolder.tvLikes.setText(" " + Utils.formatNumberForDisplay(post.likesCount) + " likes");
        // Caption
        viewHolder.tvCaption.setText(getCaptionSpan(context, post));

    }

    private SpannableStringBuilder getCaptionSpan(Context context, InstagramPost post) {
        ForegroundColorSpan blueForegroundColorSpan = new ForegroundColorSpan(
                context.getResources().getColor(R.color.blue_text));
        SpannableStringBuilder ssb = new SpannableStringBuilder(post.user.userName);
        ssb.setSpan(
                blueForegroundColorSpan,
                0,
                ssb.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (!TextUtils.isEmpty(post.caption)) {
            ssb.append(" " + post.caption);
        }
        return ssb;
    }

    @Override
    public int getItemCount() {
        return instagramPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvUsername;
        public TextView tvCreatedTime;
        public TextView tvLikes;
        public TextView tvCaption;
        public SimpleDraweeView sdvProfile;
        public SimpleDraweeView sdvPic;

        public ViewHolder(View itemView) {
            super(itemView);

            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvCreatedTime = (TextView) itemView.findViewById(R.id.tvCreatedTime);
            tvLikes = (TextView) itemView.findViewById(R.id.tvLikes);
            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
            sdvProfile = (SimpleDraweeView) itemView.findViewById(R.id.sdvProfilePic);
            sdvPic = (SimpleDraweeView) itemView.findViewById(R.id.sdvPic);

        }
    }
}
