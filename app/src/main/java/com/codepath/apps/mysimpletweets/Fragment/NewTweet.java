package com.codepath.apps.mysimpletweets.Fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.Listener.DialogListener;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.CurrentUserInfo;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class NewTweet extends DialogFragment {

    EditText txtBody;
    TextView txtName,txtScreenName,txtChars;
    Button btnSend, btnCancel;
    CurrentUserInfo userInfo;
    ImageView imgProfile;
    public int MAX_COUNT = 140;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_new_tweet, container, false);
        Bundle bundle = getArguments();
        userInfo = bundle.getParcelable("user");
        txtBody = (EditText) view.findViewById(R.id.txtMessage);
        txtName = (TextView) view.findViewById(R.id.txtName);
        txtScreenName = (TextView) view.findViewById(R.id.txtScreenName);
        txtChars = (TextView) view.findViewById(R.id.txtChars);
        btnSend = (Button) view.findViewById(R.id.btnSend);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        imgProfile = (ImageView) view.findViewById(R.id.imgProfilePic);
        getDialog().setTitle("New Tweet");


        if(userInfo!=null) {
            txtName.setText(userInfo.getUserName());
            txtScreenName.setText(userInfo.getScreenName());
            imgProfile.setImageResource(0);
            Picasso.with(getActivity()).load(userInfo.getProfileImage()).into(imgProfile);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = txtBody.getText().toString();
                DialogListener listener = (DialogListener) getActivity();
                listener.onReceive(msg);
                getDialog().dismiss();
            }
        });


        txtBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int textLength = txtBody.getText().length();
                if(textLength > MAX_COUNT){
                    Toast.makeText(getActivity(),"Max limit reached",Toast.LENGTH_SHORT).show();
                    txtBody.setEnabled(false);
                    return;
                }else {
                    int currentCount = MAX_COUNT - textLength;
                    txtChars.setText(currentCount+" chars left");
                }
            }
        });
        return view;
    }
}