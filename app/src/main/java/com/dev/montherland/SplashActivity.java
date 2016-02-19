package com.dev.montherland;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.dev.montherland.util.StaticVariables;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imgSplash = (ImageView) findViewById(R.id.imgSplash);

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(new AlphaAnimation(0.0F, 1.0F));
        animation.addAnimation(new ScaleAnimation(0.0f, 1, 0.0f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)); // Change args as desired
        animation.setDuration(1500);

        imgSplash.startAnimation(animation);

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                try {
                    dbhelp.DatabaseHelper2 dbhelp = new dbhelp.DatabaseHelper2(SplashActivity.this);
                    dbhelp.getReadableDatabase();
                    StaticVariables.database = dbhelp.getdatabase();
                    dbhelp.close();
                    if(StaticVariables.database.size() == 0) {
                        Intent in=new Intent(SplashActivity.this,Login.class);
                        finish();
                        startActivity(in);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    } else {
                        Intent in=new Intent(SplashActivity.this,NavigataionActivity.class);
                        finish();
                        startActivity(in);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                } catch (Exception e) {
                    Intent in=new Intent(SplashActivity.this,Login.class);
                    finish();
                    startActivity(in);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            }
        }.start();
    }
}


