package com.example.wsr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity {

    EditText emailTextView;
    EditText passwordTextView;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailTextView = findViewById(R.id.emailEditText);
        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        if(sharedPreferences.contains("email"))
            emailTextView.setText(sharedPreferences.getString("email",""));
        passwordTextView = findViewById(R.id.passwordEditText);
    }

    public void LogIn(View v){
        if(emailTextView.getText().toString().equals("")) {
            emailTextView.setError("Поле с email не заполнено!");
            return;
        }
        if(!emailTextView.getText().toString().contains("@")){
            emailTextView.setError("Отсутствует знак @ в поле с email");
            return;
        }
        if(passwordTextView.getText().toString().equals("")){
            passwordTextView.setError("Поле с паролем не заполнено!");
            return;
        }
        v.setEnabled(false);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonRequest = new JSONObject();
                    jsonRequest.put("email",emailTextView.getText().toString());
                    jsonRequest.put("password",passwordTextView.getText().toString());
                    JSONObject jsonAnswer = HttpConnect.GetResponseFromSite("http://mskko2021.mad.hakta.pro/api/user/login","POST",jsonRequest);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(jsonAnswer !=null)
                                {
                                    if(!jsonAnswer.isNull("error"))
                                        Toast.makeText(LoginActivity.this,jsonAnswer.getString("error"),Toast.LENGTH_LONG).show();
                                    else {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("id",jsonAnswer.getString("id"));
                                        editor.putString("email",jsonAnswer.getString("email"));
                                        editor.putString("nickName",jsonAnswer.getString("nickName"));
                                        editor.putString("avatar",jsonAnswer.getString("avatar"));
                                        editor.putString("token",jsonAnswer.getString("token"));
                                        editor.commit();
                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                            catch (Exception exception){
                                exception.printStackTrace();
                            }
                        }
                    });
                    v.post(new Runnable() {
                        @Override
                        public void run() {
                            v.setEnabled(true);
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void ClickButtonRegister(View v){
        Intent intent = new Intent(LoginActivity.this,RegisterActvity.class);
        startActivity(intent);
    }
}