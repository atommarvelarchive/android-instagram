package com.codepath.instagram.networking;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramPosts;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by araiff on 11/3/15.
 */
public class NetworkService extends IntentService {

    private AsyncHttpClient aClient = new SyncHttpClient();
    public static final String ACTION = "com.codepath.instagram.networking.NetworkService";



    public NetworkService() {
        super("NetworkService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NetworkService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final InstagramPosts posts = new InstagramPosts();
        final Intent in = new Intent(ACTION);
        final InstagramClientDatabase database = InstagramClientDatabase.getInstance(this);

        if (!MainApplication.getRestClient().isNetworkAvailable(getApplicationContext())) {
            posts.posts =  database.getAllInstagramPosts();
            in.putExtra("posts", posts);
            LocalBroadcastManager.getInstance(NetworkService.this).sendBroadcast(in);
        } else {
            SyncHttpClient syncClient = new SyncHttpClient();
            InstagramClient client = MainApplication.getRestClient();

            RequestParams params = new RequestParams("access_token", client.checkAccessToken().getToken());
            syncClient.get(this, InstagramClient.REST_URL + InstagramClient.SELF_FEED, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                    ArrayList<InstagramPost> newPosts = (ArrayList<InstagramPost>) Utils.decodePostsFromJsonResponse(responseBody);
                    posts.posts = newPosts;

                    database.emptyAllTables();
                    database.addInstagramPosts(newPosts);
                    in.putExtra("posts", posts);
                    LocalBroadcastManager.getInstance(NetworkService.this).sendBroadcast(in);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }
    }
}
