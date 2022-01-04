package com.example.wsr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Intent intent;
        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        if(sharedPreferences.contains("token"))
            intent = new Intent(this,MainActivity.class);
        else intent = new Intent(this, OnboardingActivity.class);
        startActivity(intent);
        finish();
    }
}