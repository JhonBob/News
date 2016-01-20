package com.bob.news.NewCenterDetail;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bob.news.base.MenuDetailBasePager;

/**
 * Created by Administrator on 2016/1/17.
 */
//新闻菜单对于的界面
public class TopicMenuDetailPager extends MenuDetailBasePager{

    public TopicMenuDetailPager(Context context) {
        super(context);
    }


    @Override
    public View initView() {
        TextView textView=new TextView(mContext);
        textView.setText("新闻内容");
        textView.setTextSize(20);
        textView.setTextColor(Color.BLUE);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
    }
}
