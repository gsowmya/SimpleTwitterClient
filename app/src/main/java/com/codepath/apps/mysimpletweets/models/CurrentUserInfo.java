package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Sowmya on 3/7/15.
 */
public class CurrentUserInfo implements Parcelable {
    public String getUserName() {
        return userName;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    String userName;
    String screenName;
    String profileImage;

    public static CurrentUserInfo getUserfromJSON(JSONObject jsonObject){
        CurrentUserInfo userInfo = new CurrentUserInfo();
        try {
            userInfo.userName = jsonObject.getString("name").toString();
            userInfo.screenName = jsonObject.getString("screen_name").toString();
            userInfo.profileImage = jsonObject.getString("profile_image_url").toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return userInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
