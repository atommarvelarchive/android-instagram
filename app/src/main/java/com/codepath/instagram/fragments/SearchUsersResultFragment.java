package com.codepath.instagram.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.helpers.adapters.SearchUserResultsAdapter;
import com.codepath.instagram.models.InstagramUser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by araiff on 11/1/15.
 */
public class SearchUsersResultFragment extends Fragment {
    private View mLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_searchusersresult, container, false);

        return mLayout;
    }

    public void updateQuery(String query) {
        MainApplication.getRestClient().getUserSearchResults(query, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    List<InstagramUser> users = Utils.decodeUsersFromJsonResponse(response);
                    // Lookup the recyclerview in activity layout
                    RecyclerView rvUsers = (RecyclerView) mLayout.findViewById(R.id.rvUsers);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mLayout.getContext());
                    // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                    // Handle resulting parsed JSON response here
                    // Create adapter passing in the sample user data
                    SearchUserResultsAdapter adapter = new SearchUserResultsAdapter(users);
                    // Attach the adapter to the recyclerview to populate items
                    rvUsers.setAdapter(adapter);
                    // Set layout manager to position the items
                    rvUsers.setLayoutManager(layoutManager);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Log.d("ATOM", "failed request!!!");
                }
            }
        );
    }
}

