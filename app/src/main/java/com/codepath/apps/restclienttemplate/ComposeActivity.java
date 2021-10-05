package com.codepath.apps.restclienttemplate;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {
    public static final String TAG="ComposeActivity";
    public static final int MAX_TWEET_LENGTH=280;
    EditText etCompose;
    Button btnTweet;
    TextView tvCharacterCount;
    TwitterClient client;
    int color;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client=TwitterApp.getRestClient(this);

        etCompose=findViewById(R.id.etCompose);
        btnTweet=findViewById(R.id.btnTweet);
        //Saving current color of the text
        color=btnTweet.getCurrentTextColor();
        RippleDrawable rippleDrawable = (RippleDrawable) btnTweet.getBackground();
        Drawable.ConstantState state= rippleDrawable.getConstantState();


        tvCharacterCount=findViewById(R.id.tvCharacterCount);
        tvCharacterCount.setText(String.valueOf(0) + " out of " +String.valueOf(MAX_TWEET_LENGTH));

        //Checking how long edit text is
        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCharacterCount.setText(String.valueOf(count+start) + " out of " +String.valueOf(MAX_TWEET_LENGTH));
                //Make it red if it's beyound the max length
                if (count>MAX_TWEET_LENGTH)
                    btnTweet.setTextColor(Color.parseColor("#FF0000"));
                //set it back to the original color if it is below the max.
                else
                    btnTweet.setTextColor(color);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //Set click listener
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent= etCompose.getText().toString();
                if(tweetContent.isEmpty())
                {
                    Toast.makeText(ComposeActivity.this,"Your tweet cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tweetContent.length()>MAX_TWEET_LENGTH)
                {
                    Toast.makeText(ComposeActivity.this,"Your tweet is too long",Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(ComposeActivity.this,tweetContent,Toast.LENGTH_SHORT).show();
                // Make an API call
                client.publishTweet( new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG,"Onsuccess published tweet");
                        try {
                            Tweet tweet= Tweet.fromJson(json.jsonObject);
                            //Log.i(TAG,"Published Tweet says" + tweet);
                            Intent intent=new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK,intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG,"Fail to publish tweet", throwable);
                    }
                },tweetContent);
            }
        });
    }
}