package com.example.wsr;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends Fragment {
    
    ImageView avatarImageView;
    Button exitButton;
    TextView nameTextView;
    LinearLayout container;
    SharedPreferences sharedPreferences;
    MainActivity mainActivity;

    public ProfileFragment(MainActivity mainActivity)  {
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = view.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        //Текст "Имя профиля"
        nameTextView = view.findViewById(R.id.nameTextView);
        nameTextView.setText(sharedPreferences.getString("nickName",""));
        //Установка аватара
        avatarImageView = view.findViewById(R.id.profilImageView);
        avatarImageView.setClipToOutline(true);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = HttpConnect.GetImageForURL(sharedPreferences.getString("avatar",""));
                avatarImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        avatarImageView.setImageBitmap(bitmap);
                    }
                });
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        container = view.findViewById(R.id.photoPreviewContainer);
        UpdatePhotoList();
        exitButton = view.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.Exit();
            }
        });
    }
    public void UpdatePhotoList(){
        container.removeAllViews();
        SQLiteDatabase sqLiteDatabase = mainActivity.getDBHelper().getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+DBHelper.TABLE_DATA_BASE+" Where "+DBHelper.KEY_ID_USER+" = '"+mainActivity.getSharedPreferences().getString("id","")+"';",null);
        int countElements = cursor.getCount()+1;
        int countPanels = (int)Math.ceil((double)countElements/2);
        LinearLayout[] linearLayouts = new LinearLayout[countPanels];
        for(int i = 0;i<countPanels;i++){
            LinearLayout linearLayout = new LinearLayout(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = (int)(10 * getContext().getResources().getDisplayMetrics().density);
            lp.bottomMargin = (int)(10 * getContext().getResources().getDisplayMetrics().density);
            lp.gravity = Gravity.CENTER;
            linearLayout.setLayoutParams(lp);
            container.addView(linearLayout);
            linearLayouts[i] = linearLayout;
        }
        cursor.moveToFirst();
        for(int i =0; i<countElements-1;i++){
            int numberLayout = i/2;
            PhotoPreviewView photoPreviewView = new PhotoPreviewView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = (int)(10 * getContext().getResources().getDisplayMetrics().density);
            lp.rightMargin = (int)(10 * getContext().getResources().getDisplayMetrics().density);
            photoPreviewView.setLayoutParams(lp);

            photoPreviewView.setIdPhoto(cursor.getInt(0));
            photoPreviewView.setTimeAddedPhoto(String.valueOf(cursor.getInt(1)/10)+String.valueOf(cursor.getInt(1)%10)+":"+String.valueOf(cursor.getInt(2)/10)+String.valueOf(cursor.getInt(2)%10));
            photoPreviewView.setBitmapPhoto(BitmapFactory.decodeByteArray(cursor.getBlob(3),0,cursor.getBlob(3).length));

            photoPreviewView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PhotoPreviewView pPV = (PhotoPreviewView)view;
                    mainActivity.StartPhotoActivity(pPV.getIdPhoto());
                }
            });

            linearLayouts[numberLayout].addView(photoPreviewView);
            cursor.moveToNext();
        }
        //Создаем Button для добавления нового изображения
        Button button = (Button) getLayoutInflater().inflate(R.layout.button_add_photo,null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int)(155 * getContext().getResources().getDisplayMetrics().density),(int)(115 * getContext().getResources().getDisplayMetrics().density));
        lp.leftMargin = (int)(10 * getContext().getResources().getDisplayMetrics().density);
        lp.rightMargin = (int)(10 * getContext().getResources().getDisplayMetrics().density);
        button.setLayoutParams(lp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.StartPickPicture();
            }
        });
        linearLayouts[linearLayouts.length-1].addView(button);
    }

}