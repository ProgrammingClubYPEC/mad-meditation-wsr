package com.example.wsr;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class PhotoActivity extends AppCompatActivity{

    private Bitmap bitmap;
    private int idPhoto;
    private ImageView imageView;
    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private boolean isScaled = false;

    //Для свайпа и double click
    private GestureDetector gestureDetector;
    private static final int MIN_DISTANCE = 250;
    private long lastTimeClick = 0;
    private static final int DOUBLE_CLICK_DURATION = 300;
    private float x1,x2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Bundle bundle = getIntent().getExtras();
        idPhoto = bundle.getInt("idPhoto");
        dbHelper = new DBHelper(PhotoActivity.this,DBHelper.DATA_BASE_NAME,DBHelper.DATA_BASE_VERSION);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT "+DBHelper.KEY_PHOTO_BIN+" FROM " + DBHelper.TABLE_DATA_BASE +" WHERE _id = " + String.valueOf(idPhoto)+";",null);
        if(cursor.moveToFirst())
            bitmap = BitmapFactory.decodeByteArray(cursor.getBlob(0),0,cursor.getBlob(0).length);
        imageView = findViewById(R.id.photoElementImageView);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x1 = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = motionEvent.getX();
                        float deffX = x2 - x1;
                        if(Math.abs(deffX)>MIN_DISTANCE){
                            //Свайп вправо
                            if(deffX>0) finish();
                                //Свайп влево
                            else DeletePhoto();
                        }
                        //Отлеживаем double click
                        else{
                            long timeClick = System.currentTimeMillis();
                            if(timeClick - lastTimeClick < DOUBLE_CLICK_DURATION)
                            {
                                if(isScaled){
                                    imageView.setScaleX(1);
                                    imageView.setScaleY(1);
                                    isScaled = false;
                                }
                                else {
                                    imageView.setScaleX(2);
                                    imageView.setScaleY(2);
                                    isScaled = true;
                                }
                                lastTimeClick = 0;
                                return  true;
                            }
                            lastTimeClick = timeClick;
                        }
                        break;
                }
                return true;
            }
        });
        imageView.setImageBitmap(bitmap);
        setResult(RESULT_CANCELED);
    }
    public void OnButtonCloseDeleteClick(View v){
        if(v.getId() == R.id.closeButton)
            finish();
        else{
            DeletePhoto();
        }
    }
    private void DeletePhoto(){
        sqLiteDatabase.delete(DBHelper.TABLE_DATA_BASE,"_id = "+String.valueOf(idPhoto),null);
        setResult(RESULT_OK);
        finish();
    }

}