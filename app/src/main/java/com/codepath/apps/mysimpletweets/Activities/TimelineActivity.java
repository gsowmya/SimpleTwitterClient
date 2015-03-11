package com.codepath.apps.mysimpletweets.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.Adapter.TweetAdapter;
import com.codepath.apps.mysimpletweets.Listener.DialogListener;
import com.codepath.apps.mysimpletweets.Listener.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.Fragment.NewTweet;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.Client.TwitterApplication;
import com.codepath.apps.mysimpletweets.Client.TwitterClient;
import com.codepath.apps.mysimpletweets.models.CurrentUserInfo;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends ActionBarActivity  implements DialogListener {

    private TwitterClient client;
    private TweetAdapter adapter;
    private List<Tweet> tweets;
    private ListView lvTweets;
    private SwipeRefreshLayout refreshLayout;
    int tweet_id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        client = TwitterApplication.getRestClient();
        tweets = new ArrayList<Tweet>();
        adapter = new TweetAdapter(getApplicationContext(),tweets);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(-1);
            }
        });

        populateTimeline(-1);
        // Configure the refreshing colors
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Tweet tweet =(Tweet) tweets.get(tweets.size()-1);
                populateTimeline(tweet.getUtweetid());
            }
        });
        lvTweets.setAdapter(adapter);

    }

    private void populateTimeline(long maxId){

        client.getHomeTimeline(maxId, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("success",response.toString());
                adapter.addAll(Tweet.fromJSONArray(response));
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,JSONObject responseString) {
                super.onFailure(statusCode, headers, throwable, responseString);
            }
        });
    }

    @Override
    public void onReceive(String msg) {
      client.postTweet(msg,new JsonHttpResponseHandler(){
          @Override
          public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
              Log.d("post success", response.toString());
              populateTimeline(-1);
          }

          @Override
          public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
              super.onFailure(statusCode, headers, responseString, throwable);
          }
      });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twitter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_compose) {
         final NewTweet newTweet = new NewTweet();
         client.getCurrentUserInfo(new JsonHttpResponseHandler(){
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 Log.d("user success",response.toString());
                 CurrentUserInfo userInfo = CurrentUserInfo.getUserfromJSON(response);
                 Bundle bundle = new Bundle();
                 bundle.putParcelable("user", userInfo);
                 newTweet.setArguments(bundle);
                 newTweet.show(getFragmentManager(), "Compose Tweet");
             }
         });

        }

        return super.onOptionsItemSelected(item);
    }

    public static class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
        }


        // Inflate the menu; this adds items to the action bar if it is present.
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.login, menu);
            return true;
        }

        // OAuth authenticated successfully, launch primary authenticated activity
        // i.e Display application "homepage"
        @Override
        public void onLoginSuccess() {

            Intent i = new Intent(this, TimelineActivity.class);
            startActivity(i);
        }

        // OAuth authentication flow failed, handle the error
        // i.e Display an error dialog or toast
        @Override
        public void onLoginFailure(Exception e) {
            e.printStackTrace();
        }

        // Click handler method for the button used to start OAuth flow
        // Uses the client to initiate OAuth authorization
        // This should be tied to a button used to login
        public void loginToRest(View view) {
            getClient().connect();
        }

    }
}
