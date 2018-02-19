package com.jograt.atenatics.wordplay_offlinedictionary;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
        ImageView logo1;
        ImageView logo2;
        ImageView logo3;
        ImageView logo4;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_splash);

            logo1 = (ImageView)findViewById(R.id.logo1);
            logo2 = (ImageView)findViewById(R.id.logo2);
            logo3 = (ImageView)findViewById(R.id.logo3);
            logo4 = (ImageView)findViewById(R.id.logo4);

            logo1.setAnimation(AnimationUtils.loadAnimation(this, R.anim.leftright));
            logo2.setAnimation(AnimationUtils.loadAnimation(this, R.anim.rightleft));
            logo3.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slideindown));
            logo4.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slideinup));


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.nolaman);
                finish();
            }
        },2000);
        }
}
