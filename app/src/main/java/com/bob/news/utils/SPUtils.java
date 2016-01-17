package com.bob.news.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/1/15.
 */
public class SPUtils {

    private static final String FILENAME="news";
    private static SharedPreferences mSharedPreferences;

    //欢迎引导
    public static boolean getBoolean(Context context,String key,boolean defValue){
        if(mSharedPreferences==null) {
            mSharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getBoolean(key,defValue);
    }

    public static void putBoolean(Context context,String key,boolean value){
        if(mSharedPreferences==null) {
            mSharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putBoolean(key,value).apply();
    }
}
