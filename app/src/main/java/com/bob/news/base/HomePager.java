package com.bob.news.base;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/1/16.
 */
public class HomePager extends TabBasePage {

    public HomePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        tvTitle.setText("智慧北京");
        ibMenu.setVisibility(View.GONE);

        TextView tv=new TextView(mContext);
        tv.setText("首页");
        tv.setTextSize(25);
        tv.setTextColor(Color.RED);
        tv.setGravity(Gravity.CENTER);

        flContent.addView(tv);
    }
}
