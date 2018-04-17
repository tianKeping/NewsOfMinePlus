package com.example.keping.newsofmine.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MySharedPreference {

    public static void save(String name, String value, Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "text", Activity.MODE_PRIVATE);
        Editor editor = mySharedPreferences.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static void saveBitmapToSharedPreferences(String key, Bitmap bitmap,
                                                     Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "text", Activity.MODE_PRIVATE);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 80, byteArrayOutputStream);

        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));


        Editor editor = mySharedPreferences.edit();
        editor.putString(key, imageString);
        editor.commit();
    }


    public static Bitmap getBitmapFromSharedPreferences(String key, String def,
                                                        Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "text", Activity.MODE_PRIVATE);

        String imageString = mySharedPreferences.getString(key, def);

        byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                byteArray);

        Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
        return bitmap;
    }

    public static String get(String name, String defvalue, Context context) {
        if (context == null) {
            return "";
        }
        if (name == null) {
            return "";
        }
        if (defvalue == null) {
            return "";

        }
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "text", Activity.MODE_PRIVATE);
        if (mySharedPreferences == null) {
            return "";
        } else
            return mySharedPreferences.getString(name, defvalue);
    }

    public static void clear(Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "text", Activity.MODE_PRIVATE);
        Editor editor = mySharedPreferences.edit();
        editor.remove("userId");
        editor.clear();
        editor.commit();
    }
}
