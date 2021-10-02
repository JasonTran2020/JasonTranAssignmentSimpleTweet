package com.codepath.apps.restclienttemplate;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ComposeActivity extends AppCompatActivity {
    public static final int MAX_TWEET_LENGTH=280;
    EditText etCompose;
    Button btnTweet;
    TextView tvCharacterCount;
    int color;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etCompose=findViewById(R.id.etCompose);
        btnTweet=findViewById(R.id.btnTweet);
        //Saving current color of the text
        color=btnTweet.getCurrentTextColor();

        tvCharacterCount=findViewById(R.id.tvCharacterCount);
        tvCharacterCount.setText(String.valueOf(0) + " out of " +String.valueOf(MAX_TWEET_LENGTH));

        //Checking how long edit text is
        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCharacterCount.setText(String.valueOf(count) + " out of " +String.valueOf(MAX_TWEET_LENGTH));
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
            }
        });
    }
}