package com.example.topchef;

import android.content.Intent;
import android.os.Handler;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_DELAY = 4000;
    private final Handler mHandler = new Handler();
    private final Launcher mLauncher = new Launcher();
    private class Launcher implements Runnable {
        @Override
        public void run() {
            launch();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mHandler.postDelayed(mLauncher, SPLASH_DELAY);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
    @Override
    protected void onStop() {
        mHandler.removeCallbacks(mLauncher);
        super.onStop();
    }
    private void launch() {
        if (!isFinishing()) {
            Intent intentMain = new Intent(SplashScreen.this, Login.class);
            startActivity(intentMain);
            finish();
        }
    }
}
