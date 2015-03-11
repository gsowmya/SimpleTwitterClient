package com.codepath.apps.mysimpletweets.Adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetAdapter extends ArrayAdapter<Tweet> {

    public TweetAdapter(Context context,List<Tweet> tweets){
        super(context,android.R.layout.simple_list_item_1,tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TweetViewHolder holder ;
        Tweet tweet = getItem(position);
        if (convertView == null) {
            holder = new TweetViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_item, parent, false);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtScreenName = (TextView) convertView.findViewById(R.id.txtScreenName);
            holder.imgProfile = (ImageView) convertView.findViewById(R.id.imgProfilePic);
            holder.txtBody = (TextView) convertView.findViewById(R.id.txtBody);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);

            convertView.setTag(holder);
        } else{
           holder = (TweetViewHolder) convertView.getTag();
        }

        holder.txtName.setText(tweet.getUser().getName());
        holder.txtScreenName.setText(tweet.getUser().getScreenName());
        holder.txtBody.setText(tweet.getBody());
        holder.imgProfile.setImageResource(0);
        String convertedTime = getRelativeTime(tweet.getTime());
        holder.txtTime.setText(convertedTime);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImage()).into(holder.imgProfile);

        return convertView;
    }

    private String getRelativeTime(String createdAt){
        String relativeTime=null;
        String format ="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        sdf.setLenient(true);

        try{
            long dateMillis = sdf.parse(createdAt).getTime();
            relativeTime = DateUtils.getRelativeTimeSpanString(dateMillis,System.currentTimeMillis(),
                               DateUtils.SECOND_IN_MILLIS).toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return relativeTime;

    }


     class TweetViewHolder{
        public TextView txtName;
        public ImageView imgProfile;
        public TextView txtBody;
        public TextView txtTime;
        public TextView txtScreenName;
    }
}
