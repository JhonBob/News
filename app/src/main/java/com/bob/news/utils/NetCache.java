package com.bob.news.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/1/20.
 */

//网络缓存
public class NetCache {

    private Handler mHandler;

    public static final int success=0;
    public static final int failed=1;
    private ExecutorService mExecutorService;
    private MemeryCache memeryCache;

    public NetCache(Handler handler,MemeryCache memeryCache){
        this.mHandler=handler;
        this.memeryCache=memeryCache;
        //创建一个内部有5个线程的线程池
        mExecutorService= Executors.newFixedThreadPool(5);

    }

    public void getBitmapFromNet(String url,int tag){
       // new Thread(new InternalRunable(url,tag)).start();
        //有线程池自行维护，避免创建更多线程对象
        mExecutorService.execute(new InternalRunable(url,tag));
    }


    class InternalRunable implements Runnable{

        private String url;
        private int tag;

        public InternalRunable(String url,int tag) {
            this.url=url;
            this.tag=tag;
        }

        @Override
        public void run() {
            HttpURLConnection conn=null;
            try{
                conn=(HttpURLConnection)new URL(url).openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.connect();

                int responseCode=conn.getResponseCode();
                if(responseCode==200){
                    InputStream is=conn.getInputStream();
                    Bitmap bitmap= BitmapFactory.decodeStream(is);

                    Message message=mHandler.obtainMessage();
                    message.obj=bitmap;
                    message.arg1=tag;
                    message.what=success;
                    message.sendToTarget();

                    //本地存储
                    LocalCache.putBitmap(url,bitmap);
                    //内存存储
                    memeryCache.putBitmap(url,bitmap);
                    return;
                }

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if (conn!=null){
                    conn.disconnect();
                }
            }

            mHandler.obtainMessage(failed).sendToTarget();
        }
    }
}
