package com.codepath.instagram.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.instagram.R;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.adapters.InstagramCommentsAdapter;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CommentsActivity extends AppCompatActivity {

    public static final String EXTRA_MEDIA_ID = "mediaId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        String mediaId = getIntent().getStringExtra(EXTRA_MEDIA_ID);

        MainApplication.getRestClient().getCommentsFromMediaId(mediaId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                List<InstagramComment> comments = Utils.decodeCommentsFromJsonResponse(response);
                // Lookup the recyclerview in activity layout
                RecyclerView rvPosts = (RecyclerView) findViewById(R.id.rvComments);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CommentsActivity.this);
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                // Create adapter passing in the sample user data
                InstagramCommentsAdapter adapter = new InstagramCommentsAdapter(comments);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
