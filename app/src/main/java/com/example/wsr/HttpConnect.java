package com.example.wsr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class HttpConnect {
    public static Bitmap GetImageForURL(String urlAdr) {
        Bitmap bitmap = null;
        try{
            URL url = new URL(urlAdr);
            InputStream inputStream = url.openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  bitmap;
    }
    public static JSONObject GetResponseFromSite(String site,String requestMethod,JSONObject request){
        try {
            URL url = new URL(site);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod(requestMethod);
            httpURLConnection.setRequestProperty("content-type", "application/json");
            //httpURLConnection.setDoOutput(true);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            if(request != null)httpURLConnection.getOutputStream().write(request.toString().getBytes(StandardCharsets.UTF_8));
            httpURLConnection.connect();
            InputStream inputStream;
            if(httpURLConnection.getResponseCode()==200)
                inputStream = httpURLConnection.getInputStream();
            else inputStream = httpURLConnection.getErrorStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine())!=null)
                stringBuilder.append(line);
            return new JSONObject(stringBuilder.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
}
