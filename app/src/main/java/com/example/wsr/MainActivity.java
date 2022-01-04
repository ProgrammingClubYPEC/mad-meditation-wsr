package com.example.wsr;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ActivityResultLauncher<String> photoPicker;
    Fragment currentFragment;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;
    public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }
    public DBHelper getDBHelper(){
        return  dbHelper;
    }
    //Результат из PhotoActivity
    ActivityResultLauncher<Intent> photoAct = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==RESULT_OK){
                if(currentFragment instanceof ProfileFragment)
                    ((ProfileFragment) currentFragment).UpdatePhotoList();
            }
        }
    });
    public void StartPhotoActivity(int idPhoto){
        Intent intent = new Intent(MainActivity.this,PhotoActivity.class);
        intent.putExtra("idPhoto",idPhoto);
        photoAct.launch(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        dbHelper = new DBHelper(MainActivity.this,DBHelper.DATA_BASE_NAME,DBHelper.DATA_BASE_VERSION);
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(listener);
        bottomNavigationView.setSelectedItemId(R.id.homeFragment);
        //Получение результата из файлового диспетчера
        photoPicker = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result !=null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),result);
                        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                        Date currentDate = new Date();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBHelper.KEY_TIME_HOURS,currentDate.getHours());
                        contentValues.put(DBHelper.KEY_TIME_MINUTE,currentDate.getMinutes());
                        contentValues.put(DBHelper.KEY_ID_USER, sharedPreferences.getString("id",""));

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                        contentValues.put(DBHelper.KEY_PHOTO_BIN,byteArrayOutputStream.toByteArray());

                        sqLiteDatabase.insert(DBHelper.TABLE_DATA_BASE,null,contentValues);
                        if(currentFragment instanceof ProfileFragment)
                            ((ProfileFragment) currentFragment).UpdatePhotoList();

                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

    }
    public void Exit(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("id");
        editor.remove("nickName");
        editor.remove("avatar");
        editor.remove("token");
        editor.commit();
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void StartPickPicture(){
        photoPicker.launch("image/*");
    }
    BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
            if(bottomNavigationView.getSelectedItemId() == item.getItemId() && currentFragment !=null) return false;
            Fragment fragment = null;
            switch (item.getItemId())
            {
                case R.id.homeFragment:
                    fragment = new HomeFragment(MainActivity.this);
                    break;
                case R.id.profilFragment:
                    fragment = new ProfileFragment(MainActivity.this);
                    break;
            }
            if(fragment !=null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fr, fragment).commit();
                currentFragment = fragment;
            }
            return true;
        }
    };
}