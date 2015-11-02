package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.adapters.InstagramPostsAdapter;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by araiff on 11/1/15.
 */
public class PostsFragment extends Fragment {

    private View mLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_posts, container, false);
        MainApplication.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                List<InstagramPost> posts = Utils.decodePostsFromJsonResponse(response);
                // Lookup the recyclerview in activity layout
                RecyclerView rvPosts = (RecyclerView) mLayout.findViewById(R.id.rvPosts);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mLayout.getContext());
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                // Create adapter passing in the sample user data
                InstagramPostsAdapter adapter = new InstagramPostsAdapter(posts);
                // Attach the adapter to the recyclerview to populate items
                rvPosts.setAdapter(adapter);
                // Set layout manager to position the items
                rvPosts.setLayoutManager(layoutManager);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("ATOM", "failed request!!!");
            }
        });
        return mLayout;

    }
}
