package com.codepath.instagram.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramUser;
import com.codepath.instagram.networking.InstagramClient;

/**
 * Created by araiff on 11/4/15.
 */
public class ProfileFragment extends Fragment {

    private View mLayout;
    private Context mContext;
    private InstagramUser mUser;
    private TextView tvPostsCount;
    private TextView tvFollowersCount;
    private TextView tvFollowingCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = scanForActivity(container.getContext());
        tvPostsCount = (TextView) container.findViewById(R.id.tvPostsCount);
        tvFollowersCount = (TextView) container.findViewById(R.id.tvFollowersCount);
        tvFollowingCount = (TextView) container.findViewById(R.id.tvFollowingCount);
        setupPostsFragment();
        return mLayout;
    }

    private void setupPostsFragment() {
        // Begin the transaction
        FragmentTransaction ft = ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        PostsFragment usersPosts = new PostsFragment();
        usersPosts.setPostSource(createSpecificUserPostSrc());
        ft.replace(R.id.flPosts, usersPosts);
        // Complete the changes added above
        ft.commit();
    }

    private void fillInProfileData() {
        // This is where I would fill out the data IF I have time to add those counts to the User model
    }

    public void setUser(InstagramUser user) {
        mUser = user;
        Log.e("ATOM", user.userId);
    }

    private String createSpecificUserPostSrc() {
        return InstagramClient.REST_URL + InstagramClient.USERS_RECENT_POSTS.replace(InstagramClient.USER_ID_PLACEHOLDER, mUser.userId);
    }

    private static Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity)cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper)cont).getBaseContext());
        return null;
    }
}
