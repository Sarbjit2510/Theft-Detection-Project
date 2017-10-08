package com.pragyaware.sarbjit.jkpddapp.mActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.pragyaware.sarbjit.jkpddapp.R;
import com.pragyaware.sarbjit.jkpddapp.common.PreferenceUtil;


public class SplashActivity extends Activity {

    Animation animFadeIn;
    Animation animFadeOut;
    private Context con;
    private RelativeLayout layout;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.acitivity_splash);

        con = this;
        layout = (RelativeLayout) findViewById(R.id.splash_main);

        start();
    }

    private void start() {


        animFadeIn = AnimationUtils.loadAnimation(con, R.anim.fade);
        animFadeIn.setStartOffset(200);   // delay
        layout.startAnimation(animFadeIn);      //start animation


        //set fade out animation
        animFadeOut = AnimationUtils.loadAnimation(con, R.anim.out);
        animFadeOut.setStartOffset(2000);
        animFadeIn.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // layout.startAnimation(animFadeOut);
                if (PreferenceUtil.getInstance(con).isLoggedIn()) {
                    startMainActivity();
                } else {
                    finish();
                    startActivity(new Intent(con, RegisterUserActivity.class));
                }


            }
        });

        animFadeOut.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                finish();

                startActivity(new Intent(con, RegisterUserActivity.class));


                overridePendingTransition(R.anim.enter, R.anim.out);

            }
        });

    }

    private void startMainActivity() {
        finish();
        if (PreferenceUtil.getInstance(this).isOfficer()) {
            Intent intent = new Intent(getApplicationContext(), OfficerDashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), UserDashBoardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

}
