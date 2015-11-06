package com.codepath.instagram.helpers.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.codepath.instagram.R;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.fragments.PostsFragment;
import com.codepath.instagram.fragments.ProfileFragment;
import com.codepath.instagram.fragments.SearchFragment;
import com.codepath.instagram.fragments.SearchTagsResultFragment;
import com.codepath.instagram.fragments.SearchUsersResultFragment;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramUser;
import com.codepath.instagram.networking.InstagramClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by araiff on 11/1/15.
 */
public class HomeFragmentStatePagerAdapter extends SmartFragmentStatePagerAdapter {

    private final Context context;
    private InstagramUser consumer = new InstagramUser();
    private ProfileFragment profFragment;
    private int[] imageResId = {
            R.drawable.ic_home,
            R.drawable.ic_search,
            R.drawable.ic_capture,
            R.drawable.ic_notifs,
            R.drawable.ic_profile
    };


    public HomeFragmentStatePagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
        profFragment = new ProfileFragment();
        consumer.userId = "self";
        getConsumer();
    }

    private void getConsumer() {
        MainApplication.getRestClient().getUserFromId("self", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                consumer = InstagramUser.fromJson(Utils.getDataJsonObj(response));
                profFragment.setUser(consumer);
            }
        });
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new SearchFragment();
            case 4:
                profFragment.setUser(consumer);
                return profFragment;
            default:
                PostsFragment homeFragment = new PostsFragment();
                homeFragment.setPostSource(createHomeTimelineSrc());
                return homeFragment;
        }
    }

    private String createHomeTimelineSrc() {
        return InstagramClient.REST_URL + InstagramClient.SELF_FEED;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
