package com.example.wsr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
    }
    public void OnClickRegisterTextView(View v){
        Intent intent = new Intent(getApplicationContext(),RegisterActvity.class);
        startActivity(intent);
    }
    public void OnClickLoginButton(View v){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }
}