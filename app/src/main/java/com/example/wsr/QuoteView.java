package com.example.wsr;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;


public class QuoteView extends ConstraintLayout {

    public int getIdQuote() {
        return idQuote;
    }

    public void setIdQuote(int idQuote) {
        this.idQuote = idQuote;
    }

    public String getTitleQuote() {
        return titleQuote;
    }

    public void setTitleQuote(String titleQuote) {
        this.titleQuote = titleQuote;
        titleTextView.setText(titleQuote);
    }

    public String getDesQuote() {
        return desQuote;
    }

    public void setDesQuote(String desQuote) {
        this.desQuote = desQuote;
        desTextView.setText(desQuote);
    }

    private int idQuote;
    private String titleQuote;
    private String desQuote;

    private ImageView quoteImageView;
    private TextView titleTextView;
    private TextView desTextView;
    public void SetImage(String url){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = HttpConnect.GetImageForURL(url);
                post(new Runnable() {
                    @Override
                    public void run() {
                        quoteImageView.setImageBitmap(bitmap);
                    }
                });
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public QuoteView(Context context) {
        super(context);
        initialization(context);
    }

    public QuoteView(Context context,AttributeSet attrs) {
        super(context, attrs);
        initialization(context);
    }

    public QuoteView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialization(context);
    }

    public QuoteView(Context context,AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialization(context);
    }
    private void initialization(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_quote,this);
        quoteImageView = findViewById(R.id.quoteImageView);
        titleTextView = findViewById(R.id.quoteTitleTextView);
        desTextView = findViewById(R.id.quoteDescriptionTextView);
        setBackground(ContextCompat.getDrawable(context,R.drawable.background_quote));
    }
}
