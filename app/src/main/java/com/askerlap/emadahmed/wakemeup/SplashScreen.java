package com.askerlap.emadahmed.wakemeup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
ImageView logo;
//TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        logo=(ImageView)findViewById(R.id.viewSplash);
//        title=(ImageView)findViewById(R.id.tv_loading);
        Animation fade1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise);
//        logo.setAnimation(animation);
//        name=(TextView)findViewById(R.id.iv_appName);
//        Animation name_Animation=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.custom_anim);
//        name.setAnimation(name_Animation);
        assert getSupportActionBar()!=null;
        getSupportActionBar().hide();

        Thread timer =new Thread(){
          public void run() {


                  try {
                      sleep(6000);

                  } catch (InterruptedException e) {
                      e.printStackTrace();

                  } finally {
                      Intent i = new Intent(SplashScreen.this, MainActivity.class);
                      startActivity(i);
                  }

          }

        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
