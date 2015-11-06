package com.codepath.instagram.networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.codepath.instagram.helpers.Constants;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;


/**
 * Created by araiff on 10/28/15.
 */
public class InstagramClient extends OAuthBaseClient {

    private static final String MEDIA_ID_PLACEHOLDER = "{media-id}";
    public static final String USER_ID_PLACEHOLDER = "{user-id}";
    private static final String POPULAR_ENTRY_POINT = "https://api.instagram.com/v1/media/popular";
    private static final String COMMENTS_ENTRY_POINT = "/media/" + MEDIA_ID_PLACEHOLDER + "/comments";
    public static final String USERS_RECENT_POSTS = "users/"+USER_ID_PLACEHOLDER+"/media/recent/";
    private static final String USER_DATA_ENTRY_POINT = "users/"+USER_ID_PLACEHOLDER+"/?access_token=ACCESS-TOKEN";
    private static final String CLIENT_ID = "7f5321002cc04089b778e463cd87953f";
    private static final String CLIENT_SECRET = "a9980e6933814fd3848dba9f6b370b63";
    public static final String REST_URL = "https://api.instagram.com/v1/";
    public static final String SELF_FEED = "users/self/feed";
    private static final String USER_SEARCH = "users/search";
    private static final String TAG_SEARCH = "tags/search";

    private static final Class<? extends Api> REST_API_CLASS = InstagramApi.class;
    private static final String REST_CONSUMER_KEY = CLIENT_ID;
    private static final String REST_CONSUMER_SECRET = CLIENT_SECRET;
    private static final String REDIRECT_URI = Constants.REDIRECT_URI;
    private static final String SCOPE = Constants.SCOPE;

    public InstagramClient(Context context) {
        super(context, REST_API_CLASS, REST_URL,
                REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REDIRECT_URI, SCOPE);
    }

    public void getPopularFeed(JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("client_id", CLIENT_ID);
        client.get(POPULAR_ENTRY_POINT, params, responseHandler);
    }

    public void getPostsFromSource(String src, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("access_token", client.getAccessToken().getToken());
        client.get(src, params, responseHandler);
    }

    public Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void getCommentsFromMediaId(String mediaId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("client_id", CLIENT_ID);
        client.get(REST_URL + COMMENTS_ENTRY_POINT.replace(MEDIA_ID_PLACEHOLDER, mediaId), params, responseHandler);
    }

    public void getHomeTimeline(JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("access_token", client.getAccessToken().getToken());
        client.get(REST_URL + SELF_FEED, params, responseHandler);
    }

    public void getUserSearchResults(String query, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("access_token", client.getAccessToken().getToken());
        params.put("q", query);
        client.get(REST_URL + USER_SEARCH, params, responseHandler);
    }

    public void getTagSearchResults(String query, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("access_token", client.getAccessToken().getToken());
        params.put("q", query);
        client.get(REST_URL + TAG_SEARCH, params, responseHandler);
    }

    public void getUserFromId(String userId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("access_token", client.getAccessToken().getToken());
        client.get(REST_URL + USER_DATA_ENTRY_POINT.replace(USER_ID_PLACEHOLDER, userId), params, responseHandler);
    }
}
