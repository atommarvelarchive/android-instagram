package com.codepath.instagram.helpers.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.CommentsActivity;
import com.codepath.instagram.helpers.ImageUtils;
import com.codepath.instagram.helpers.UIUtils;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramImage;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramUser;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
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
        View postView = inflater.inflate(R.layout.layout_item_post, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Context context = viewHolder.sdvProfilePic.getContext();
        InstagramPost post = instagramPosts.get(position);
        InstagramUser user = post.user;
        final InstagramImage image = post.image;

        // Set item views based on the data model
        // Username
        viewHolder.tvUsername.setText(user.userName);
        // Created Time
        viewHolder.tvCreatedTime.setText(DateUtils.getRelativeTimeSpanString(post.createdTime * 1000));
        // Profile picture
        viewHolder.sdvProfilePic.setImageURI(Uri.parse(user.profilePictureUrl));
        // Picture
        viewHolder.sdvPic.setImageURI(Uri.parse(image.imageUrl));
        viewHolder.sdvPic.setAspectRatio(image.imageWidth / image.imageHeight);
        // Likes
        viewHolder.tvLikes.setText(" " + Utils.formatNumberForDisplay(post.likesCount) + " likes");
        // Caption
        viewHolder.tvCaption.setText(UIUtils.getCommentSpan(context, post.user.userName, post.caption));
        // Comments
        renderComments(context, viewHolder.llComments, post);
        // Share
        viewHolder.ibMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtils.getBitmapFromUri(context, Uri.parse(image.imageUrl), new ImageUtils.ImageUtilCallback() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap) {
                        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                bitmap, "Image Description", null);
                        Uri bmpUri = Uri.parse(path);
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        shareIntent.setType("image/*");
                        context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
                    }
                });
            }
        });


    }

    private void renderComments(final Context context, LinearLayout llComments, final InstagramPost post) {
        List<InstagramComment> comments = post.comments;
        int commentCount = post.commentsCount;
        // Clear out old comments
        llComments.removeAllViews();

        // If there are more than 2 comments, add a clickable TextView that allows you to view all comments in a new activity
        if (commentCount > 2) {
            TextView tvViewComments = new TextView(context);
            tvViewComments.setText("View all " + Utils.formatNumberForDisplay(commentCount) + " comments");
            tvViewComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Launch Activity that lets you view all the comments
                    Intent intent = new Intent(context, CommentsActivity.class);
                    intent.putExtra(CommentsActivity.EXTRA_MEDIA_ID, post.mediaId);
                    context.startActivity(intent);
                }
            });
            llComments.addView(tvViewComments);
        }

        // Add comments to LinearLayout
        if (commentCount > 0) {
            int commentRenderCount = commentCount >= 2 ? 2 : 1;
            List<InstagramComment> commentsToRender = new ArrayList<>(comments.subList(0, commentRenderCount));
            for (InstagramComment comment : commentsToRender) {
                View commentView = LayoutInflater.from(context).inflate(R.layout.layout_item_text_comment, llComments, false);
                TextView tvComment = (TextView) commentView.findViewById(R.id.tvComment);
                tvComment.setText(UIUtils.getCommentSpan(context, comment.user.userName, comment.text));
                llComments.addView(commentView);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        instagramPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<InstagramPost> list) {
        instagramPosts.addAll(list);
        notifyDataSetChanged();
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
        public SimpleDraweeView sdvProfilePic;
        public SimpleDraweeView sdvPic;
        public LinearLayout llComments;
        public ImageButton ibMore;

        public ViewHolder(View itemView) {
            super(itemView);

            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvCreatedTime = (TextView) itemView.findViewById(R.id.tvCreatedTime);
            tvLikes = (TextView) itemView.findViewById(R.id.tvLikes);
            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
            sdvProfilePic = (SimpleDraweeView) itemView.findViewById(R.id.sdvProfilePic);
            sdvPic = (SimpleDraweeView) itemView.findViewById(R.id.sdvPic);
            llComments = (LinearLayout) itemView.findViewById(R.id.llComments);
            ibMore = (ImageButton) itemView.findViewById(R.id.ibMore);

        }
    }
}
