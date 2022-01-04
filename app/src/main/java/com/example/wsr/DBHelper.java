package com.example.wsr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    //Информация о БД
    public static final  int DATA_BASE_VERSION = 18;
    public static final String DATA_BASE_NAME = "photoDb";
    public static final String TABLE_DATA_BASE = "photo";

    //Информация о таблице
    public static final String KEY_ID = "_id";
    public static final String KEY_TIME_HOURS = "hours";
    public static final String KEY_TIME_MINUTE = "minute";
    public static final String KEY_ID_USER = "userok";
    public static final String KEY_PHOTO_BIN ="photo_bin";

    public DBHelper(@Nullable Context context, @Nullable String name,int version) {
        super(context, name, null, DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create Table " + TABLE_DATA_BASE+
                "("+KEY_ID+" integer primary key autoincrement, "+
                KEY_TIME_HOURS +" integer, "+
                KEY_TIME_MINUTE +" integer, " +
                KEY_PHOTO_BIN + " BLOB , "+
                KEY_ID_USER + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE if exists " + TABLE_DATA_BASE);
            onCreate(sqLiteDatabase);
    }
}
