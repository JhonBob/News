package com.bob.news.utils;

/**
 * Created by Administrator on 2016/1/17.
 */

//常量类
public class Constancts {
    //模拟器地址：serveiceurl="http://10.0.2.2:8080/zhbj"
    //Genymotion地址1：serveiceurl="http://192.168.56.1:8080/zhbj"
    //Genymotion地址2：serveiceurl="http://10.0.3.2:8080/zhbj"
    public static final String serveiceurl="http://10.0.3.2:8080/zhbj";
    //新闻中心地址
    public static String newscenter_url=serveiceurl+"/categories.json";
    //组图地址
    public static String photo_url=serveiceurl+"/photos/photos_1.json";
}
