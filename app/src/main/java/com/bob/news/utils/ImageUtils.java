package com.bob.news.utils;

import android.graphics.Bitmap;
import android.os.Handler;


/**
 * Created by Administrator on 2016/1/20.
 */
public class ImageUtils {
    private NetCache mNetCache;
    private MemeryCache memeryCache;

    public ImageUtils(Handler handler) {
        //内存缓存
        memeryCache=new MemeryCache();
        //网络缓存
        mNetCache=new NetCache(handler,memeryCache);

    }

    public Bitmap getImageFromUtils(String url,int tag){

        Bitmap bitmap=memeryCache.getBitmap(url);
        if (bitmap!=null){
            return bitmap;
        }


        bitmap=LocalCache.getBitmap(url);
        if (bitmap!=null){
            return bitmap;
        }

        mNetCache.getBitmapFromNet(url,tag);
        return null;
    }
}
