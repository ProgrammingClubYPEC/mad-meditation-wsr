package com.example.wsr;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class HomeFragment extends Fragment {
    ImageView avatarImageView;
    TextView returnTextView;
    LinearLayout feelingLinearLayout;
    LinearLayout quotesLinearLayout;
    MainActivity mainActivity;


    public HomeFragment(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Текст "С возвращением"
        returnTextView = view.findViewById(R.id.returnTextView);
        returnTextView.setText(getString(R.string.main_return_text)+mainActivity.getSharedPreferences().getString("nickName","")+"!");
        //Установка аватара
        avatarImageView = view.findViewById(R.id.profilImageView);
        avatarImageView.setClipToOutline(true);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = HttpConnect.GetImageForURL(mainActivity.getSharedPreferences().getString("avatar",""));
                avatarImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        avatarImageView.setImageBitmap(bitmap);
                    }
                });
            }
        };
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomNavigationView bottomNavigationView = mainActivity.findViewById(R.id.bottomNav);
                bottomNavigationView.setSelectedItemId(R.id.profilFragment);
            }
        });
        Thread thread = new Thread(runnable);
        thread.start();
        GetFeelings(view);
        GetQuote(view);
    }
    //Получаем советы
    private void GetQuote(View v){
        quotesLinearLayout = v.findViewById(R.id.quoteLinearLayout);
        Runnable quoteRun = new Runnable() {
            @Override
            public void run() {
                JSONObject jsonAnswer = HttpConnect.GetResponseFromSite("http://mskko2021.mad.hakta.pro/api/quotes","GET",null);
                quotesLinearLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(jsonAnswer.getBoolean("success")){
                                JSONArray jsonArray = jsonAnswer.getJSONArray("data");
                                for(int i = 0; i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    QuoteView quoteView = new QuoteView(v.getContext());
                                    quoteView.setId(jsonObject.getInt("id"));
                                    quoteView.setDesQuote(jsonObject.getString("description"));
                                    quoteView.setTitleQuote(jsonObject.getString("title"));
                                    quoteView.SetImage(jsonObject.getString("image"));
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    layoutParams.topMargin = (int)(12* v.getContext().getApplicationContext().getResources().getDisplayMetrics().density);
                                    layoutParams.bottomMargin = (int)(12* v.getContext().getApplicationContext().getResources().getDisplayMetrics().density);
                                    quoteView.setLayoutParams(layoutParams);
                                    quotesLinearLayout.addView(quoteView);
                                }
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        Thread quoteThread = new Thread(quoteRun);
        quoteThread.start();
    }
    //Получаем статусы
    private void GetFeelings(View v){
        feelingLinearLayout = v.findViewById(R.id.feelingLinearLayout);
        Runnable feelingRunnable = new Runnable() {
            @Override
            public void run() {
                JSONObject jsonAnswer = HttpConnect.GetResponseFromSite("http://mskko2021.mad.hakta.pro/api/feelings","GET",null);
                feelingLinearLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(jsonAnswer.getBoolean("success")){
                                JSONArray jsonArray = jsonAnswer.getJSONArray("data");
                                //Сортировка
                                JSONObject[] jsonObjects = new JSONObject[jsonArray.length()];
                                for(int j = 0; j<jsonArray.length();j++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                                    jsonObjects[jsonObject.getInt("position")-1] = jsonObject;
                                }
                                for(int i = 0; i<jsonObjects.length;i++){
                                    JSONObject jsonObject = jsonObjects[i];
                                    FeelingView feelingView = new FeelingView(v.getContext());
                                    feelingView.setId(jsonObject.getInt("id"));
                                    feelingView.setPosition(jsonObject.getInt("position"));
                                    feelingView.setTitleFeeling(jsonObject.getString("title"));
                                    feelingView.SetImage(jsonObject.getString("image"));
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    layoutParams.rightMargin = (int)(12* v.getContext().getResources().getDisplayMetrics().density);
                                    layoutParams.leftMargin = (int)(12* v.getContext().getResources().getDisplayMetrics().density);
                                    feelingView.setLayoutParams(layoutParams);
                                    feelingLinearLayout.addView(feelingView);
                                }
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        Thread feelingThread = new Thread(feelingRunnable);
        feelingThread.start();
    }
}