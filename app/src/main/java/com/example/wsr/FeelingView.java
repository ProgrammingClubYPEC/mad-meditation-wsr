package com.example.wsr;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class FeelingView extends LinearLayout {

    public String getTitleFeeling() {
        return titleFeeling;
    }

    public void setTitleFeeling(String titleFeeling) {
        this.titleFeeling = titleFeeling;
        feelingTextView.setText(titleFeeling);
    }

    public int getIdFeeling() {
        return idFeeling;
    }

    public void setIdFeeling(int idFeeling) {
        this.idFeeling = idFeeling;
    }
    ImageView feelingImageView;
    TextView feelingTextView;
    public void SetImage(String url){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = HttpConnect.GetImageForURL(url);
                post(new Runnable() {
                    @Override
                    public void run() {
                        feelingImageView.setImageBitmap(bitmap);
                    }
                });
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private int position;
    private String titleFeeling;
    private int idFeeling;
    public FeelingView(Context context) {
        super(context);
        initialization(context);
    }

    public FeelingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialization(context);
    }

    public FeelingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialization(context);
    }

    public FeelingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialization(context);
    }
    private void initialization(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_feeling,this);
        feelingImageView  = findViewById(R.id.feelingImageView);
        feelingTextView = findViewById(R.id.feelingTextView);
        this.setOrientation(VERTICAL);
    }
}
