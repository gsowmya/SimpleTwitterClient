package com.codepath.apps.mysimpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getUtweetid() {
        return utweetid;
    }

    public void setUtweetid(long utweetid) {
        this.utweetid = utweetid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String body;
    private long utweetid;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;
    private String time;

    public static Tweet fromJSON(JSONObject jsonObject){
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.utweetid = jsonObject.getLong("id");
            tweet.time = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

        }catch (JSONException e){
            e.printStackTrace();
        }
        return tweet;
    }

    public static List<Tweet> fromJSONArray(JSONArray jsonArray){
        List<Tweet> tweets = new ArrayList<Tweet>();

        for(int i=0;i<jsonArray.length();i++){

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(jsonObject);
                if(tweet!=null){
                    tweets.add(tweet);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return  tweets;
    }

}
