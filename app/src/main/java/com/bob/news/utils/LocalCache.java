package com.bob.news.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016/1/20.
 */
public class LocalCache {

    private static final File cacheDir=new File("/mnt/sdcard/news");

    public static void putBitmap(String url,Bitmap bm){
        try{
            String fileName=MD5Encoder.encode(url).substring(0,10);
            if (!cacheDir.exists()){
                cacheDir.mkdir();
            }
            File cacheFile=new File(cacheDir,fileName);
            FileOutputStream fos=new FileOutputStream(cacheFile);
            bm.compress(Bitmap.CompressFormat.JPEG,100,fos);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmap(String url){
        try{
            String fileName=MD5Encoder.encode(url).substring(0,10);
            File cacheFile=new File(cacheDir,fileName);
            if (cacheFile.exists()){
                return BitmapFactory.decodeFile(cacheFile.getPath());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
