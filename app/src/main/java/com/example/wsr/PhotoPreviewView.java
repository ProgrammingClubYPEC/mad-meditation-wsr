package com.example.wsr;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.jetbrains.annotations.NotNull;

public class PhotoPreviewView extends ConstraintLayout {

    public int getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(int idPhoto) {
        this.idPhoto = idPhoto;
    }

    public String getTimeAddedPhoto() {
        return timeAddedPhoto;
    }

    public void setTimeAddedPhoto(String timeAddedPhoto) {
        this.timeAddedPhoto = timeAddedPhoto;
        timeTextView.setText(timeAddedPhoto);
    }

    public Bitmap getBitmapPhoto() {
        return bitmapPhoto;
    }

    public void setBitmapPhoto(Bitmap bitmapPhoto) {
        this.bitmapPhoto = bitmapPhoto;
        imageViewPhoto.setImageBitmap(bitmapPhoto);
    }

    private int idPhoto;
    private String timeAddedPhoto;
    private Bitmap bitmapPhoto;

    private ImageView imageViewPhoto;
    private TextView timeTextView;

    public PhotoPreviewView(@NonNull @NotNull Context context) {
        super(context);
        initialization(context);
    }

    public PhotoPreviewView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        initialization(context);
    }

    public PhotoPreviewView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialization(context);
    }

    public PhotoPreviewView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialization(context);
    }
    private void initialization(Context context){
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.layout_photo_preview_view,this);
        timeTextView = findViewById(R.id.timeTextView);
        imageViewPhoto = findViewById(R.id.photoImageView);
        imageViewPhoto.setClipToOutline(true);

    }
}
