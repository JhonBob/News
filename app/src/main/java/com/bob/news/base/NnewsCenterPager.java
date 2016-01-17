package com.bob.news.base;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/1/16.
 */
public class NnewsCenterPager extends TabBasePage {

    public NnewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        tvTitle.setText("新闻中心");
        ibMenu.setVisibility(View.VISIBLE);

        TextView tv=new TextView(mContext);
        tv.setText("新闻");
        tv.setTextSize(25);
        tv.setTextColor(Color.RED);
        tv.setGravity(Gravity.CENTER);

        flContent.addView(tv);
    }
}
