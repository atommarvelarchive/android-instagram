package com.codepath.instagram.networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


/**
 * Created by araiff on 10/28/15.
 */
public class InstagramClient {

    private static final String MEDIA_ID_PLACEHOLDER = "{media-id}";
    private static final String POPULAR_ENTRY_POINT = "https://api.instagram.com/v1/media/popular";
    private static final String COMMENTS_ENTRY_POINT = "https://api.instagram.com/v1/media/" + MEDIA_ID_PLACEHOLDER + "/comments";
    private static final String CLIENT_ID = "INSERT YOU CLIENT HERE";

    public static void getPopularFeed(JsonHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("client_id", CLIENT_ID);
        client.get(POPULAR_ENTRY_POINT, params, responseHandler);
    }

    private static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void getCommentsFromMediaId(String mediaId, JsonHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("client_id", CLIENT_ID);
        client.get(COMMENTS_ENTRY_POINT.replace(MEDIA_ID_PLACEHOLDER, mediaId), params, responseHandler);
    }


}
