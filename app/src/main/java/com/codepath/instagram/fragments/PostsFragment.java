package com.codepath.instagram.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.EndlessScrollListener;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.helpers.adapters.InstagramPostsAdapter;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramPosts;
import com.codepath.instagram.networking.NetworkService;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by araiff on 11/1/15.
 */
public class PostsFragment extends Fragment {

    private Context mContext;
    private View mLayout;
    private String mSrc;
    private String mNextSrc;
    private RecyclerView rvPosts;
    private SwipeRefreshLayout swipeContainer;
    private InstagramPostsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private JsonHttpResponseHandler handler =  new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            List<InstagramPost> posts = Utils.decodePostsFromJsonResponse(response);
            mNextSrc = Utils.getNextSrc(response);
            /*InstagramClientDatabase db =  InstagramClientDatabase.getInstance(mContext);
            db.emptyAllTables();
            db.addInstagramPosts(posts);*/
            loadUI(posts);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            Log.d("ATOM", "failed request!!!");
        }

    };
    private JsonHttpResponseHandler infiniteHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            List<InstagramPost> posts = Utils.decodePostsFromJsonResponse(response);
            appendToList(posts);
            mNextSrc = Utils.getNextSrc(response);

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_posts, container, false);
        mContext = container.getContext();
        initUI();
        if (MainApplication.getRestClient().isNetworkAvailable(mContext)) {
            MainApplication.getRestClient().getPostsFromSource(mSrc, handler);
        } else {
            /*InstagramClientDatabase db =  InstagramClientDatabase.getInstance(mContext);
            List<InstagramPost> posts  = db.getAllInstagramPosts();
            loadUI(posts);*/
        }
        return mLayout;
    }

    public void setPostSource(String src) {
        mSrc = src;
    }

    private void initUI() {
        layoutManager = new LinearLayoutManager(mLayout.getContext());
        rvPosts = (RecyclerView) mLayout.findViewById(R.id.rvPosts);
        swipeContainer = (SwipeRefreshLayout) mLayout.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().startService(new Intent(getActivity(), NetworkService.class));
                //MainApplication.getRestClient().getHomeTimeline(handler);
            }
        });
        rvPosts.setOnScrollListener(new EndlessScrollListener(layoutManager) {
            @Override
            public boolean onLoadMore(int page) {
                if (!mNextSrc.isEmpty()) {
                    MainApplication.getRestClient().getPostsFromSource(mNextSrc, infiniteHandler);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void loadUI(List<InstagramPost> posts) {
        // Lookup the recyclerview in activity layout

        // Root JSON in response is an dictionary i.e { "data : [ ... ] }
        // Handle resulting parsed JSON response here
        // Create adapter passing in the sample user data
        adapter = new InstagramPostsAdapter(posts);
        // Attach the adapter to the recyclerview to populate items
        rvPosts.setAdapter(adapter);
        // Set layout manager to position the items
        rvPosts.setLayoutManager(layoutManager);
        swipeContainer.setRefreshing(false);
    }

    private void appendToList(List<InstagramPost> posts) {
        int newItemIdx = adapter.getItemCount();
        adapter.addAll(posts);
        adapter.notifyItemRangeInserted(newItemIdx, posts.size());
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(postsReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NetworkService.ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(postsReceiver, filter);
    }

    private BroadcastReceiver postsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            InstagramPosts posts = (InstagramPosts) intent.getSerializableExtra("posts");
            loadUI(posts.posts);
        }
    };
}
