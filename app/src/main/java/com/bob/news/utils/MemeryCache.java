package com.bob.news.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by Administrator on 2016/1/20.
 */
public class MemeryCache {

    public LruCache<String,Bitmap> mMCache;

    public MemeryCache(){
        int maxMemory=(int)(Runtime.getRuntime().maxMemory()/80);
        mMCache=new LruCache<String,Bitmap>(maxMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public void putBitmap(String url,Bitmap bitmap){
        mMCache.put(url, bitmap);
    }

    public Bitmap getBitmap(String url){
        return mMCache.get(url);
    }
}


