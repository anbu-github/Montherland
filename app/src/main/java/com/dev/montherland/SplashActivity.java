package com.dev.montherland;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private ImageView imgSplash;
    String islogin;
    private final String DEFAULT = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imgSplash = (ImageView) findViewById(R.id.imgSplash);

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(new AlphaAnimation(0.0F, 1.0F));
        animation.addAnimation(new ScaleAnimation(0.0f, 1, 0.0f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)); // Change args as desired
        animation.setDuration(1500);

        imgSplash.startAnimation(animation);

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);  //First arg is filename and second arg is value i.e. private i.e. only app can access this file
                islogin=sharedPreferences.getString("islogin", DEFAULT);  //First arg is key and second arg is value. If key/value doesnt exist then DEFAULT will be returned

                if (islogin.equals("loggedIn")){
                    Intent in=new Intent(SplashActivity.this,NavigataionActivity.class);
                    finish();
                    startActivity(in);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else {
                    Intent in=new Intent(SplashActivity.this,Login.class);
                    finish();
                    startActivity(in);
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                }
            }
        }.start();
    }
}


