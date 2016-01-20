package com.bob.news.base;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2016/1/17.
 */

//显示菜单对应页面的基类
public abstract class MenuDetailBasePager {
    public Context mContext;
    public View rootView;

    public MenuDetailBasePager(Context context) {
        this.mContext=context;
        rootView=initView();
    }

    public abstract View initView();

    public View getRootView(){
        return rootView;
    }

    public void initData(){}
}
